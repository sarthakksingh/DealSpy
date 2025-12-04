package com.example.dealspy.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.repo.SaveForLaterRepo
import com.example.dealspy.data.repo.SearchRepo
import com.example.dealspy.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepo: SearchRepo,
    private val saveForLaterRepo: SaveForLaterRepo
) : ViewModel() {

    private val _searchList = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val searchList = _searchList.asStateFlow()

    private val _saveForLaterState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val saveForLaterState = _saveForLaterState.asStateFlow()

    fun searchProductList(productName: String) {
        viewModelScope.launch {
            _searchList.value = UiState.Loading
            try {
                val response = searchRepo.searchProducts(productName)
                if (response.success && response.data != null) {
                    val products = response.data  // ← Direct List<Product> from API
                    _searchList.value = if (products.isEmpty()) {
                        UiState.NoData
                    } else {
                        UiState.Success(products)
                    }
                    Log.d("SearchViewModel", "Search successful: ${products.size} products")
                } else {
                    _searchList.value = UiState.Error(response.message ?: "Failed to get search results")
                }
            } catch (e: UnknownHostException) {
                Log.e("SearchViewModel", "Network error", e)
                _searchList.value = UiState.NoInternet
            } catch (e: retrofit2.HttpException) {
                Log.e("SearchViewModel", "Server error", e)
                _searchList.value = UiState.ServerError
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error searching products", e)
                _searchList.value = UiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    // toggleSaveForLater and resetSaveForLaterState remain unchanged
    fun toggleSaveForLater(product: Product) {
        viewModelScope.launch {
            try {
                _saveForLaterState.value = UiState.Loading
                val response = saveForLaterRepo.addToSaveForLater(product)
                if (response.success) {
                    _saveForLaterState.value = UiState.Success("Added to Save for Later")
                    Log.d("SearchViewModel", "Added ${product.name} to save for later")
                } else {
                    _saveForLaterState.value = UiState.Error(response.message ?: "Failed to save")
                }
            } catch (e: UnknownHostException) {
                _saveForLaterState.value = UiState.NoInternet
                Log.e("SearchViewModel", "No internet connection", e)
            } catch (e: Exception) {
                _saveForLaterState.value = UiState.Error(e.message ?: "Something went wrong")
                Log.e("SearchViewModel", "Error toggling save for later", e)
            }
        }
    }

    fun resetSaveForLaterState() {
        _saveForLaterState.value = UiState.Idle
    }
}
