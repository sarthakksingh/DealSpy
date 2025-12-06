package com.example.dealspy.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.repo.SaveForLaterRepo
import com.example.dealspy.data.repo.UserRepo
import com.example.dealspy.ui.state.UiState
import com.google.firebase.auth.FirebaseAuth
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
    private val userRepository: UserRepo,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _wishlist = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val wishlist = _wishlist.asStateFlow()

    private val _deleteUserState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val deleteUserState = _deleteUserState.asStateFlow()

    init {
        getWishlistProducts()
    }

    fun getWishlistProducts() {
        viewModelScope.launch {
            try {
                _wishlist.value = UiState.Loading
                Log.d("ProfileViewModel", "Loading wishlist (save for later)...")

                val response = saveForLaterRepo.getSaveForLater()
                if (response.success && response.data != null) {
                    Log.d(
                        "ProfileViewModel",
                        "Wishlist loaded: ${response.data.size} items"
                    )
                    _wishlist.value = if (response.data.isEmpty()) {
                        UiState.NoData
                    } else {
                        UiState.Success(response.data)
                    }
                } else {
                    _wishlist.value =
                        UiState.Error(response.message ?: "Failed to fetch wishlist")
                }
            } catch (e: UnknownHostException) {
                Log.e("ProfileViewModel", "Network error", e)
                _wishlist.value = UiState.NoInternet
            } catch (e: HttpException) {
                Log.e("ProfileViewModel", "Server error", e)
                _wishlist.value = UiState.ServerError
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading wishlist", e)
                _wishlist.value =
                    UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun removeFromWishlist(productName: String) {
        viewModelScope.launch {
            try {
                Log.d("ProfileViewModel", "Removing from wishlist: $productName")
                val response = saveForLaterRepo.removeFromSaveForLater(productName)
                if (response.success) {
                    Log.d("ProfileViewModel", "Successfully removed from wishlist")
                    getWishlistProducts()
                } else {
                    Log.e("ProfileViewModel", "Failed to remove: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error removing from wishlist", e)
            }
        }
    }

    fun deleteUserAccount() {
        viewModelScope.launch {
            try {
                _deleteUserState.value = UiState.Loading
                Log.d("ProfileViewModel", "Attempting to delete user account")

                val response = userRepository.deleteUser()
                if (response.success) {
                    Log.d("ProfileViewModel", "Account deleted successfully")
                    _deleteUserState.value =
                        UiState.Success(response.message ?: "Account deleted successfully")
                } else {
                    Log.e("ProfileViewModel", "Delete failed: ${response.message}")
                    _deleteUserState.value =
                        UiState.Error(response.message ?: "Failed to delete account")
                }
            } catch (e: UnknownHostException) {
                Log.e("ProfileViewModel", "Network error deleting account", e)
                _deleteUserState.value = UiState.NoInternet
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error deleting account", e)
                _deleteUserState.value =
                    UiState.Error(e.message ?: "Failed to delete account")
            }
        }
    }

    fun resetDeleteState() {
        _deleteUserState.value = UiState.Idle
    }

    fun refresh() {
        getWishlistProducts()
    }

    fun getUserDisplayName(): String {
        return auth.currentUser?.displayName ?: "User"
    }

    fun getUserPhotoUrl(): String? {
        return auth.currentUser?.photoUrl?.toString()
    }

    fun getUserEmail(): String? {
        return auth.currentUser?.email
    }
}
