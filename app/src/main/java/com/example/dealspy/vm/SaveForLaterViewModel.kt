// Create: vm/SaveForLaterViewModel.kt
package com.example.dealspy.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.model.SaveForLater
import com.example.dealspy.data.model.UiProduct
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

    private val _saveForLaterState = MutableStateFlow<UiState<List<UiProduct>>>(UiState.Idle)
    val saveForLaterState = _saveForLaterState.asStateFlow()

    private val _addToSaveForLaterState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val addToSaveForLaterState = _addToSaveForLaterState.asStateFlow()

    private val _removeFromSaveForLaterState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val removeFromSaveForLaterState = _removeFromSaveForLaterState.asStateFlow()

    init {
        // 🔹 AUTOMATICALLY LOAD DATA WHEN VIEWMODEL IS CREATED
        loadSaveForLater()
    }

    fun loadSaveForLater() {
        viewModelScope.launch {
            try {
                _saveForLaterState.value = UiState.Loading
                Log.d("SaveForLaterViewModel", "Loading save for later items...")

                val response = saveForLaterRepository.getSaveForLater()

                if (response.success == true && response.data != null) {
                    Log.d("SaveForLaterViewModel", "Save for later loaded: ${response.data.size} items")

                    // 🔹 CONVERT SaveForLater (DTO) → Product → UiProduct
                    val uiProducts = response.data.map { saveForLaterItem ->
                        // Convert SaveForLaterDTO to Product
                        val product = Product(
                            name = saveForLaterItem.productName,
                            platformName = extractPlatformFromDesc(saveForLaterItem.desc),
                            priceRaw = extractPriceFromDesc(saveForLaterItem.desc),
                            deepLink = extractDeepLinkFromDesc(saveForLaterItem.desc),
                            imageURL = saveForLaterItem.imageUrl,
                            discount = null,
                            lastKnownPrice = 0
                        )

                        // Convert to UiProduct (no time constraint for save for later)
                        UiProduct(
                            product = product,
                            brand = extractPlatformFromDesc(saveForLaterItem.desc),
                            timeLeftMillis = Long.MAX_VALUE // No time limit for saved items
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

                // 🔹 CONVERT Product → SaveForLaterDTO
                val saveForLaterItem = SaveForLater(
                    productName = product.name,
                    imageUrl = product.imageURL,
                    desc = createDescFromProduct(product)
                )

                val response = saveForLaterRepository.addToSaveForLater(saveForLaterItem)

                if (response.success == true) {
                    Log.d("SaveForLaterViewModel", "Added to save for later successfully")
                    _addToSaveForLaterState.value = UiState.Success(Unit)
                    loadSaveForLater() // Refresh
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
                    loadSaveForLater() // Refresh
                } else {
                    _removeFromSaveForLaterState.value = UiState.Error(response.message ?: "Failed to remove from save for later")
                }
            } catch (e: Exception) {
                Log.e("SaveForLaterViewModel", "Error removing from save for later", e)
                _removeFromSaveForLaterState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    // 🔹 USER ACTION METHODS
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

    // 🔹 HELPER METHODS
    private fun extractPlatformFromDesc(desc: String): String {
        // Parse platform name from description
        // Example: "Amazon - Electronics - ₹1000" -> "Amazon"
        return desc.substringBefore(" - ").takeIf { it.isNotBlank() } ?: "Unknown"
    }

    private fun extractPriceFromDesc(desc: String): String {
        // Try to extract price from description
        val priceRegex = "₹[\\d,]+".toRegex()
        return priceRegex.find(desc)?.value ?: "₹0"
    }

    private fun extractDeepLinkFromDesc(desc: String): String {
        // Try to extract deep link from description if stored
        val urlRegex = "https?://[^\\s]+".toRegex()
        return urlRegex.find(desc)?.value ?: ""
    }

    private fun createDescFromProduct(product: Product): String {
        // Create description from product info
        return "${product.platformName} - ${product.priceRaw} - ${product.name} - ${product.deepLink}"
    }

    fun resetAddState() {
        _addToSaveForLaterState.value = UiState.Idle
    }

    fun resetRemoveState() {
        _removeFromSaveForLaterState.value = UiState.Idle
    }

    fun refresh() {
        loadSaveForLater()
    }
}
