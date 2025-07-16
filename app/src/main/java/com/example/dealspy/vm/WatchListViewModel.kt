package com.example.dealspy.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.model.UiProduct
import com.example.dealspy.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import java.io.IOException
import javax.inject.Inject

class WatchListViewModel @Inject constructor() : ViewModel() {

    private val _watchListState = MutableStateFlow<UiState<List<UiProduct>>>(UiState.Loading)
    val watchListState = _watchListState.asStateFlow()

    fun loadWatchlist() {
        viewModelScope.launch {
            _watchListState.value = UiState.Loading
            try {
                val products = getWatchlistFromRepository()

                // Convert Product → UiProduct
                val uiProducts = products.map { product ->
                    UiProduct(
                        product = product,
                        brand = product.platformName,
                        timeLeftMillis = 2 * 60 * 60 * 1000 // Example: 2 hours
                    )
                }

                _watchListState.value = if (uiProducts.isEmpty()) {
                    UiState.NoData
                } else {
                    UiState.Success(uiProducts)
                }
            } catch (e: IOException) {
                _watchListState.value = UiState.NoInternet
            } catch (e: HttpException) {
                _watchListState.value = UiState.ServerError
            } catch (e: Exception) {
                _watchListState.value = UiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    private suspend fun getWatchlistFromRepository(): List<Product> {
        // 🔧 Replace this with real API/Firebase logic
        return listOf()
    }

    fun onProductClick(product: Product) {
        // TODO: Handle navigation
    }

    fun onAddProduct() {
        // TODO: Handle navigation
    }

    fun onDeleteProduct(product: Product) {
        // TODO: Remove from DB
    }
}
