package com.example.dealspy.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.model.UserDetail
import com.example.dealspy.data.repo.SaveForLaterRepository
import com.example.dealspy.ui.state.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val saveForLaterRepository: SaveForLaterRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _userProfile = MutableStateFlow<UserDetail?>(null)
    val userProfile = _userProfile.asStateFlow()

    private val _profileState = MutableStateFlow<UiState<UserDetail>>(UiState.Idle)
    val profileState = _profileState.asStateFlow()

    private val _saveForLaterState = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val saveForLaterState = _saveForLaterState.asStateFlow()

    init {
        loadCurrentUser()
        loadUserProfile()
    }

    private fun loadCurrentUser() {
        val user = auth.currentUser
        _currentUser.value = user
        Log.d("ProfileViewModel", "Current user: ${user?.displayName}, Email: ${user?.email}")
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _profileState.value = UiState.Loading
                _saveForLaterState.value = UiState.Loading
                Log.d("ProfileViewModel", "Loading user profile...")

                val response = saveForLaterRepository.getUserProfile()

                if (response.success && response.data != null) {
                    Log.d("ProfileViewModel", "Profile loaded successfully")
                    Log.d("ProfileViewModel", "Watchlist items: ${response.data.watchList.size}")
                    Log.d("ProfileViewModel", "Save for later items: ${response.data.saveForLater.size}")

                    _userProfile.value = response.data
                    _profileState.value = UiState.Success(response.data)

                    convertSaveForLaterToProducts(response.data.saveForLater)

                } else {
                    Log.e("ProfileViewModel", "Failed to load profile: ${response.message}")

                    if (response.data != null) {
                        _userProfile.value = response.data
                        _profileState.value = UiState.Success(response.data)
                        convertSaveForLaterToProducts(response.data.saveForLater)
                    } else {
                        _profileState.value = UiState.Error(response.message ?: "Failed to load profile")
                        _saveForLaterState.value = UiState.NoData
                    }
                }
            } catch (e: UnknownHostException) {
                Log.e("ProfileViewModel", "Network error loading profile", e)
                _profileState.value = UiState.NoInternet
                _saveForLaterState.value = UiState.NoInternet
            } catch (e: retrofit2.HttpException) {
                Log.e("ProfileViewModel", "Server error loading profile", e)
                _profileState.value = UiState.ServerError
                _saveForLaterState.value = UiState.ServerError
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading profile", e)

                val existingProfile = _userProfile.value
                if (existingProfile != null) {
                    convertSaveForLaterToProducts(existingProfile.saveForLater)
                } else {
                    _profileState.value = UiState.Error(e.message ?: "Unknown error occurred")
                    _saveForLaterState.value = UiState.NoData
                }
            }
        }
    }

    private fun convertSaveForLaterToProducts(saveForLaterItems: List<com.example.dealspy.data.model.SaveForLater>) {
        try {
            if (saveForLaterItems.isEmpty()) {
                _saveForLaterState.value = UiState.NoData
                Log.d("ProfileViewModel", "No save for later items found")
                return
            }

            val products = saveForLaterItems.map { saveForLaterItem ->
                Product(
                    name = saveForLaterItem.productName,
                    platformName = extractPlatformFromDesc(saveForLaterItem.desc ?: ""),
                    priceRaw = extractPriceFromDesc(saveForLaterItem.desc ?: ""),
                    deepLink = extractDeepLinkFromDesc(saveForLaterItem.desc ?: ""),
                    imageURL = saveForLaterItem.imageUrl,
                    discount = null,
                    lastKnownPrice = 0
                )
            }

            _saveForLaterState.value = UiState.Success(products)
            Log.d("ProfileViewModel", "Converted ${products.size} save for later items to products")

        } catch (e: Exception) {
            Log.e("ProfileViewModel", "Error converting save for later data", e)
            _saveForLaterState.value = UiState.NoData
        }
    }

    fun loadSaveForLater() {
        loadUserProfile()
    }

    fun onDeleteFromSaveForLater(product: Product) {
        viewModelScope.launch {
            try {
                Log.d("ProfileViewModel", "Removing ${product.name} from save for later")
                val response = saveForLaterRepository.removeFromSaveForLater(product.name)

                if (response.success) {
                    Log.d("ProfileViewModel", "Successfully removed ${product.name}")
                    loadUserProfile()
                } else {
                    Log.e("ProfileViewModel", "Failed to remove item: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error removing item from save for later", e)
            }
        }
    }

    fun onClearSaveForLater() {
        viewModelScope.launch {
            try {
                val profile = _userProfile.value
                if (profile != null && profile.saveForLater.isNotEmpty()) {
                    Log.d("ProfileViewModel", "Clearing all save for later items")
                    profile.saveForLater.forEach { item ->
                        saveForLaterRepository.removeFromSaveForLater(item.productName)
                    }
                    loadUserProfile()
                } else {
                    Log.d("ProfileViewModel", "No items to clear")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error clearing save for later", e)
            }
        }
    }

    fun getUserDisplayName(): String {
        return _currentUser.value?.displayName ?: "User"
    }

    fun getUserPhotoUrl(): String? {
        return _currentUser.value?.photoUrl?.toString()
    }

    fun getUserEmail(): String? {
        return _currentUser.value?.email
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

    fun refresh() {
        loadCurrentUser()
        loadUserProfile()
    }
}
