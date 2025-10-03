// Create: vm/SaveForLaterViewModel.kt
package com.example.dealspy.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.model.SaveForLater
import com.example.dealspy.data.model.UiProduct
import com.example.dealspy.data.model.UserDetail
import com.example.dealspy.data.repo.SaveForLaterRepository
import com.example.dealspy.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SaveForLaterViewModel @Inject constructor(
    private val saveForLaterRepository: SaveForLaterRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserDetail?>(null)
    val userProfile = _userProfile.asStateFlow()

    private val _profileState = MutableStateFlow<UiState<UserDetail>>(UiState.Idle)
    val profileState = _profileState.asStateFlow()

    private val _saveForLaterState = MutableStateFlow<UiState<List<UiProduct>>>(UiState.Idle)
    val saveForLaterState = _saveForLaterState.asStateFlow()

    private val _addToSaveForLaterState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val addToSaveForLaterState = _addToSaveForLaterState.asStateFlow()

    private val _removeFromSaveForLaterState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val removeFromSaveForLaterState = _removeFromSaveForLaterState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _profileState.value = UiState.Loading
                _saveForLaterState.value = UiState.Loading
                Log.d("SaveForLaterViewModel", "Loading user profile...")

                val response = saveForLaterRepository.getUserProfile()

                if (response.success && response.data != null) {
                    Log.d("SaveForLaterViewModel", "Profile loaded successfully")
                    Log.d("SaveForLaterViewModel", "Watchlist items: ${response.data.watchList.size}")
                    Log.d("SaveForLaterViewModel", "Save for later items: ${response.data.saveForLater.size}")

                    _userProfile.value = response.data
                    _profileState.value = UiState.Success(response.data)

                    convertSaveForLaterToUiProducts(response.data.saveForLater)
                } else {
                    Log.e("SaveForLaterViewModel", "Failed to load profile: ${response.message}")
                    _profileState.value = UiState.Error(response.message ?: "Failed to load profile")
                    _saveForLaterState.value = UiState.Error(response.message ?: "Failed to load profile")
                }
            } catch (e: UnknownHostException) {
                Log.e("SaveForLaterViewModel", "Network error loading profile", e)
                _profileState.value = UiState.NoInternet
                _saveForLaterState.value = UiState.NoInternet
            } catch (e: retrofit2.HttpException) {
                Log.e("SaveForLaterViewModel", "Server error loading profile", e)
                _profileState.value = UiState.ServerError
                _saveForLaterState.value = UiState.ServerError
            } catch (e: Exception) {
                Log.e("SaveForLaterViewModel", "Error loading profile", e)
                _profileState.value = UiState.Error(e.message ?: "Unknown error occurred")
                _saveForLaterState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun convertSaveForLaterToUiProducts(saveForLaterItems: List<SaveForLater>) {
        try {
            if (saveForLaterItems.isEmpty()) {
                _saveForLaterState.value = UiState.NoData
                return
            }

            val uiProducts = saveForLaterItems.map { saveForLaterItem ->
                val product = Product(
                    name = saveForLaterItem.productName,
                    platformName = extractPlatformFromDesc(saveForLaterItem.desc),
                    priceRaw = extractPriceFromDesc(saveForLaterItem.desc),
                    deepLink = extractDeepLinkFromDesc(saveForLaterItem.desc),
                    imageURL = saveForLaterItem.imageUrl,
                    discount = null,
                    lastKnownPrice = 0
                )
                UiProduct(
                    product = product,
                    brand = extractPlatformFromDesc(saveForLaterItem.desc),
                    timeLeftMillis = Long.MAX_VALUE
                )
            }

            _saveForLaterState.value = UiState.Success(uiProducts)
            Log.d("SaveForLaterViewModel", "Converted ${uiProducts.size} save for later items to UiProducts")
        } catch (e: Exception) {
            Log.e("SaveForLaterViewModel", "Error converting save for later data", e)
            _saveForLaterState.value = UiState.Error("Failed to load saved items")
        }
    }
    fun loadSaveForLater() {
        val existingProfile = _userProfile.value
        if (existingProfile != null) {
            Log.d("SaveForLaterViewModel", "Using existing profile data for save for later")
            convertSaveForLaterToUiProducts(existingProfile.saveForLater)
            return
        }
        viewModelScope.launch {
            try {
                _saveForLaterState.value = UiState.Loading
                Log.d("SaveForLaterViewModel", "Loading save for later items via direct API...")

                val response = saveForLaterRepository.getSaveForLater()

                if (response.success == true && response.data != null) {
                    Log.d("SaveForLaterViewModel", "Save for later loaded: ${response.data.size} items")

                    val uiProducts = response.data.map { saveForLaterItem ->
                        val product = Product(
                            name = saveForLaterItem.productName,
                            platformName = extractPlatformFromDesc(saveForLaterItem.desc),
                            priceRaw = extractPriceFromDesc(saveForLaterItem.desc),
                            deepLink = extractDeepLinkFromDesc(saveForLaterItem.desc),
                            imageURL = saveForLaterItem.imageUrl,
                            discount = null,
                            lastKnownPrice = 0
                        )

                        UiProduct(
                            product = product,
                            brand = extractPlatformFromDesc(saveForLaterItem.desc),
                            timeLeftMillis = Long.MAX_VALUE
                        )
                    }

                    _saveForLaterState.value = if (uiProducts.isEmpty()) {
                        UiState.NoData
                    } else {
                        UiState.Success(uiProducts)
                    }
                } else {
                    _saveForLaterState.value = UiState.Error(response.message ?: "Failed to load save for later")
                }
            } catch (e: UnknownHostException) {
                Log.e("SaveForLaterViewModel", "Network error", e)
                _saveForLaterState.value = UiState.NoInternet
            } catch (e: retrofit2.HttpException) {
                Log.e("SaveForLaterViewModel", "Server error", e)
                _saveForLaterState.value = UiState.ServerError
            } catch (e: Exception) {
                Log.e("SaveForLaterViewModel", "Error loading save for later", e)
                _saveForLaterState.value = UiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun addToSaveForLater(product: Product) {
        viewModelScope.launch {
            try {
                _addToSaveForLaterState.value = UiState.Loading
                Log.d("SaveForLaterViewModel", "Adding to save for later: ${product.name}")

                val saveForLaterItem = SaveForLater(
                    productName = product.name,
                    imageUrl = product.imageURL,
                    desc = createDescFromProduct(product)
                )

                val response = saveForLaterRepository.addToSaveForLater(saveForLaterItem)

                if (response.success == true) {
                    Log.d("SaveForLaterViewModel", "Added to save for later successfully")
                    _addToSaveForLaterState.value = UiState.Success(Unit)
                    loadUserProfile()
                } else {
                    _addToSaveForLaterState.value = UiState.Error(response.message ?: "Failed to add to save for later")
                }
            } catch (e: Exception) {
                Log.e("SaveForLaterViewModel", "Error adding to save for later", e)
                _addToSaveForLaterState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun removeFromSaveForLater(productName: String) {
        viewModelScope.launch {
            try {
                _removeFromSaveForLaterState.value = UiState.Loading
                Log.d("SaveForLaterViewModel", "Removing from save for later: $productName")

                val response = saveForLaterRepository.removeFromSaveForLater(productName)

                if (response.success == true) {
                    Log.d("SaveForLaterViewModel", "Removed from save for later successfully")
                    _removeFromSaveForLaterState.value = UiState.Success(Unit)
                    loadUserProfile()
                } else {
                    _removeFromSaveForLaterState.value = UiState.Error(response.message ?: "Failed to remove from save for later")
                }
            } catch (e: Exception) {
                Log.e("SaveForLaterViewModel", "Error removing from save for later", e)
                _removeFromSaveForLaterState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun onProductClick(product: Product) {
        Log.d("SaveForLaterViewModel", "Product clicked: ${product.name}")
        // Handle navigation or open deep link
    }

    fun onDeleteProduct(product: Product) {
        removeFromSaveForLater(product.name)
    }

    fun onMoveToWatchlist(product: Product) {
        // TODO: Add to watchlist and remove from save for later
        Log.d("SaveForLaterViewModel", "Move to watchlist: ${product.name}")
    }

    fun getWatchlistItems(): List<com.example.dealspy.data.model.WatchList> {
        return _userProfile.value?.watchList ?: emptyList()
    }

    fun getSaveForLaterItems(): List<SaveForLater> {
        return _userProfile.value?.saveForLater ?: emptyList()
    }

    private fun extractPlatformFromDesc(desc: String): String {
        return desc.substringBefore(" - ").takeIf { it.isNotBlank() } ?: "Unknown"
    }

    private fun extractPriceFromDesc(desc: String): String {
        val priceRegex = "₹[\\d,]+".toRegex()
        return priceRegex.find(desc)?.value ?: "₹0"
    }

    private fun extractDeepLinkFromDesc(desc: String): String {
        val urlRegex = "https?://[^\\s]+".toRegex()
        return urlRegex.find(desc)?.value ?: ""
    }

    private fun createDescFromProduct(product: Product): String {
        return "${product.platformName} - ${product.priceRaw} - ${product.name} - ${product.deepLink}"
    }

    fun resetAddState() {
        _addToSaveForLaterState.value = UiState.Idle
    }

    fun resetRemoveState() {
        _removeFromSaveForLaterState.value = UiState.Idle
    }

    fun resetProfileState() {
        _profileState.value = UiState.Idle
    }

    fun refresh() {
        loadUserProfile()
    }
}
