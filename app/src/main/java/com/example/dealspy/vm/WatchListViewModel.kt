package com.example.dealspy.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.model.UiProduct
import com.example.dealspy.data.model.WatchList
import com.example.dealspy.data.repo.GeminiRepo
import com.example.dealspy.data.repo.WatchListRepo
import com.example.dealspy.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WatchListViewModel @Inject constructor(
    private val watchListRepo: WatchListRepo,
    private val geminiRepo: GeminiRepo
) : ViewModel() {

    private val _watchListState = MutableStateFlow<UiState<List<UiProduct>>>(UiState.Loading)
    val watchListState = _watchListState.asStateFlow()

    fun loadWatchlist() {
        viewModelScope.launch {
            _watchListState.value = UiState.Loading
            try {
                val response = watchListRepo.getWatchlist()

                if (response.success) {
                    val watchlist = response.data ?: emptyList()
                    val products = geminiRepo.fetchProductsFromPrompt("")

                    val uiProducts = products.map { product ->
                        val time = watchlist.timeLeft
                        UiProduct(
                            product = product, // Assuming you have an extension or conversion
                            brand = product.platformName,
                            timeLeftMillis = (time?.days ?: 0) * 24 * 60 * 60 * 1000L +
                                    (time?.hours ?: 0) * 60 * 60 * 1000L +
                                    (time?.min ?: 0) * 60 * 1000L +
                                    (time?.sec ?: 0) * 1000L
                        )
                    }

                    _watchListState.value = if (uiProducts.isEmpty()) {
                        UiState.NoData
                    } else {
                        UiState.Success(uiProducts)
                    }
                } else {
                    _watchListState.value = UiState.Failed(response.message ?: "Unknown error")
                }
            } catch (e: IOException) {
                _watchListState.value = UiState.NoInternet
            } catch (e: HttpException) {
                _watchListState.value = UiState.ServerError
            } catch (e: Exception) {
                _watchListState.value = UiState.Failed(e.message ?: "Something went wrong")
            }
        }
    }

    fun onAddProduct(product: WatchList) {
        viewModelScope.launch {
            try {
                watchListRepo.addProductToWatchlist(product)
                loadWatchlist() // Refresh after adding
            } catch (e: Exception) {
                // Handle failure if necessary
            }
        }
    }

    fun onDeleteProduct(product: WatchList) {
        viewModelScope.launch {
            try {
                watchListRepo.removeProductFromWatchlist(product.productName)
                loadWatchlist() // Refresh after deleting
            } catch (e: Exception) {
                // Handle failure if necessary
            }
        }
    }
}
