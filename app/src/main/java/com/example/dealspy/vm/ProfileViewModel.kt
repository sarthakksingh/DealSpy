package com.example.dealspy.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.model.SaveForLater
import com.example.dealspy.data.model.UserDetail
import com.example.dealspy.data.model.WatchList
import com.example.dealspy.data.repo.SaveForLaterRepo
import com.example.dealspy.data.repo.UserRepo
import com.example.dealspy.data.repo.WatchListRepo
import com.example.dealspy.ui.state.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val saveForLaterRepo: SaveForLaterRepo,
    private val watchlistRepo: WatchListRepo,
    private val userRepo: UserRepo,
    private val auth: FirebaseAuth
) : ViewModel() {


    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _userProfile = MutableStateFlow<UserDetail?>(null)
    val userProfile = _userProfile.asStateFlow()

    private val _profileState = MutableStateFlow<UiState<UserDetail>>(UiState.Idle)
    val profileState = _profileState.asStateFlow()

    private val _saveForLaterState = MutableStateFlow<UiState<List<SaveForLater>>>(UiState.Loading)
    val saveForLaterState = _saveForLaterState.asStateFlow()

    // Add this state flow for watchlist operations
    private val _addToWatchlistState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val addToWatchlistState = _addToWatchlistState.asStateFlow()

    private val _deleteUserState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val deleteUserState = _deleteUserState.asStateFlow()


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

                val response = saveForLaterRepo.getUserProfile()

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
            } catch (e: HttpException) {
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

    fun addToWatchlist(product: Product) {
        viewModelScope.launch {
            try {
                _addToWatchlistState.value = UiState.Loading

                Log.d("ProfileViewModel", "Adding ${product.name} to watchlist")
                val watchlistItem = WatchList(
                    productName = product.name,
                    watchEndDate = null,
                    imageUrl = product.imageURL,
                    deepLink = product.deepLink
                )

                watchlistRepo.addToWatchlist(watchlistItem)
                _addToWatchlistState.value = UiState.Success("Added to watchlist!")

            } catch (e: UnknownHostException) {
                _addToWatchlistState.value = UiState.NoInternet
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error adding to watchlist", e)
                _addToWatchlistState.value = UiState.Error(e.message ?: "Failed to add to watchlist")
            }
        }
    }

    private fun createDescFromProduct(product: Product): String {
        return "${product.platformName} - ${product.priceRaw} - ${product.name}"
    }

    fun resetAddToWatchlistState() {
        _addToWatchlistState.value = UiState.Idle
    }

    fun deleteUserAccount() {
        viewModelScope.launch {
            try {
                _deleteUserState.value = UiState.Loading
                Log.d("ProfileViewModel", "Attempting to delete user account")

                val response = userRepo.deleteUser()
                if (response.success == true) {
                    Log.d("ProfileViewModel", "Account deleted successfully")
                    _deleteUserState.value = UiState.Success(
                        response.message ?: "Account deleted successfully"
                    )
                } else {
                    Log.e("ProfileViewModel", "Delete failed: ${response.message}")
                    _deleteUserState.value = UiState.Error(
                        response.message ?: "Failed to delete account"
                    )
                }
            } catch (e: UnknownHostException) {
                Log.e("ProfileViewModel", "Network error deleting account", e)
                _deleteUserState.value = UiState.NoInternet
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error deleting account", e)
                _deleteUserState.value = UiState.Error(
                    e.message ?: "Failed to delete account"
                )
            }
        }
    }

    fun resetDeleteState() {
        _deleteUserState.value = UiState.Idle
    }


    private fun convertSaveForLaterToProducts(saveForLaterItems: List<SaveForLater>) {
        try {
            if (saveForLaterItems.isEmpty()) {
                _saveForLaterState.value = UiState.NoData
                Log.d("ProfileViewModel", "No save for later items found")
                return
            }

            val products = saveForLaterItems.map { saveForLaterItem ->
                SaveForLater(
                    productName = saveForLaterItem.productName,
                    imageURL = saveForLaterItem.imageURL,
                    deepLink = saveForLaterItem.deepLink
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
                val response = saveForLaterRepo.removeFromSaveForLater(product.name)

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

    fun getUserDisplayName(): String {
        return _currentUser.value?.displayName ?: "User"
    }

    fun getUserPhotoUrl(): String? {
        return _currentUser.value?.photoUrl?.toString()
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
