package com.example.dealspy.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.dealspy.BuildConfig
import com.example.dealspy.data.model.Product
import com.example.dealspy.ui.state.UiState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import java.io.IOException
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _wishlistState = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val wishlistState = _wishlistState.asStateFlow()

    private val _purchaseHistoryState = MutableStateFlow<UiState<List<Pair<Product, String>>>>(UiState.Loading)
    val purchaseHistoryState = _purchaseHistoryState.asStateFlow()

    fun loadWishlist() {
        viewModelScope.launch {
            _wishlistState.value = UiState.Loading
            try {
                val wishlist = getWishlistFromRepository()
                _wishlistState.value = if (wishlist.isEmpty()) {
                    UiState.NoData
                } else {
                    UiState.Success(wishlist)
                }
            } catch (e: IOException) {
                _wishlistState.value = UiState.NoInternet
            } catch (e: HttpException) {
                _wishlistState.value = UiState.ServerError
            } catch (e: Exception) {
                _wishlistState.value = UiState.Failed(e.message ?: "Something went wrong")
            }
        }
    }

    fun loadPurchaseHistory() {
        viewModelScope.launch {
            _purchaseHistoryState.value = UiState.Loading
            try {
                val history = getPurchaseHistoryFromRepository()
                _purchaseHistoryState.value = if (history.isEmpty()) {
                    UiState.NoData
                } else {
                    UiState.Success(history)
                }
            } catch (e: IOException) {
                _purchaseHistoryState.value = UiState.NoInternet
            } catch (e: HttpException) {
                _purchaseHistoryState.value = UiState.ServerError
            } catch (e: Exception) {
                _purchaseHistoryState.value = UiState.Failed(e.message ?: "Something went wrong")
            }
        }
    }

    fun onDeleteFromWishlist(product: Product) {
        // TODO: remove product from backend
    }

    fun onClearWatchlist() {
        // TODO: clear both wishlist & purchase history from backend
    }

    // 🔧 Dummy repo calls for now (replace with Firebase/your API)
    private suspend fun getWishlistFromRepository(): List<Product> {
        return listOf() // Replace with real data
    }

    private suspend fun getPurchaseHistoryFromRepository(): List<Pair<Product, String>> {
        return listOf() // Replace with real data (e.g., Pair(product, date))
    }
}
