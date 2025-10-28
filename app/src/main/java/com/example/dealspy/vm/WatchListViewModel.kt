package com.example.dealspy.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.model.UiProduct
import com.example.dealspy.data.model.WatchList
import com.example.dealspy.data.repo.WatchListRepo
import com.example.dealspy.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class WatchListViewModel @Inject constructor(
    private val watchlistRepo: WatchListRepo
) : ViewModel() {

    private val _watchListState = MutableStateFlow<UiState<List<UiProduct>>>(UiState.Idle)
    val watchListState = _watchListState.asStateFlow()

    private val _addToWatchlistState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val addToWatchlistState = _addToWatchlistState.asStateFlow()

    private val _removeFromWatchlistState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val removeFromWatchlistState = _removeFromWatchlistState.asStateFlow()

    private val _clearWatchlistState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val clearWatchlistState = _clearWatchlistState.asStateFlow()

    init {
        loadWatchlist()
    }

    fun loadWatchlist() {
        viewModelScope.launch {
            try {
                _watchListState.value = UiState.Loading
                Log.d("WatchListViewModel", "Loading watchlist...")

                val response = watchlistRepo.getWatchlist()

                if (response.success == true && response.data != null) {
                    Log.d("WatchListViewModel", "Watchlist loaded: ${response.data.size} items")

                    val uiProducts = response.data.map { watchlistItem ->
                        val product = Product(
                            name = watchlistItem.productName,
                            deepLink = "", // Extract from desc if available
                            imageURL = watchlistItem.imageUrl,
                            discount = null,
                            lastKnownPrice = 0
                        )

                        // Convert to UiProduct with time left calculation
                        UiProduct(
                            product = product,
                            timeLeftMillis = calculateTimeLeft(watchlistItem.watchEndDateParsed)
                        )
                    }

                    _watchListState.value = if (uiProducts.isEmpty()) {
                        UiState.NoData
                    } else {
                        UiState.Success(uiProducts)
                    }
                } else {
                    _watchListState.value =
                        UiState.Error(response.message ?: "Failed to load watchlist")
                }
            } catch (e: UnknownHostException) {
                Log.e("WatchListViewModel", "Network error", e)
                _watchListState.value = UiState.NoInternet
            } catch (e: retrofit2.HttpException) {
                Log.e("WatchListViewModel", "Server error", e)
                _watchListState.value = UiState.ServerError
            } catch (e: Exception) {
                Log.e("WatchListViewModel", "Error loading watchlist", e)
                _watchListState.value = UiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun addToWatchlist(product: Product, watchEndDate: LocalDate? = null) {
        viewModelScope.launch {
            try {
                _addToWatchlistState.value = UiState.Loading
                Log.d("WatchListViewModel", "Adding to watchlist: ${product.name}")

                val watchlistItem = WatchList(
                    productName = product.name,
                    watchEndDate = null,
                    imageUrl = product.imageURL,
                    deepLink = product.deepLink
                )

                val response = watchlistRepo.addToWatchlist(watchlistItem)

                if (response.success == true) {
                    Log.d("WatchListViewModel", "Added to watchlist successfully")
                    _addToWatchlistState.value = UiState.Success(Unit)
                    loadWatchlist() // Refresh
                } else {
                    _addToWatchlistState.value =
                        UiState.Error(response.message ?: "Failed to add to watchlist")
                }
            } catch (e: Exception) {
                Log.e("WatchListViewModel", "Error adding to watchlist", e)
                _addToWatchlistState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun removeFromWatchlist(productName: String) {
        viewModelScope.launch {
            try {
                _removeFromWatchlistState.value = UiState.Loading
                Log.d("WatchListViewModel", "Removing from watchlist: $productName")

                val response = watchlistRepo.removeFromWatchlist(productName)

                if (response.success == true) {
                    Log.d("WatchListViewModel", "Removed from watchlist successfully")
                    _removeFromWatchlistState.value = UiState.Success(Unit)
                    loadWatchlist() // Refresh
                } else {
                    _removeFromWatchlistState.value =
                        UiState.Error(response.message ?: "Failed to remove from watchlist")
                }
            } catch (e: Exception) {
                Log.e("WatchListViewModel", "Error removing from watchlist", e)
                _removeFromWatchlistState.value =
                    UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun calculateTimeLeft(watchEndDate: LocalDate?): Long {
        return if (watchEndDate != null) {
            val endTime =
                watchEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val currentTime = System.currentTimeMillis()
            maxOf(0L, endTime - currentTime)
        } else {

            30 * 24 * 60 * 60 * 1000L
        }
    }

    fun clearAllWatchlist() {
        viewModelScope.launch {
            try {
                _clearWatchlistState.value = UiState.Loading
                Log.d("WatchListViewModel", "Clearing all watchlist items...")

                // Use your repository naming pattern
                val response = watchlistRepo.clearAllWatchlist() // Note: using your repo name

                if (response.success) {
                    _clearWatchlistState.value = UiState.Success(
                        response.message ?: "All watchlist items cleared successfully"
                    )

                    loadWatchlist()

                    Log.d("WatchListViewModel", "Successfully cleared all watchlist items")
                } else {
                    _clearWatchlistState.value = UiState.Error(
                        response.message ?: "Failed to clear watchlist"
                    )
                }

            } catch (e: Exception) {
                _clearWatchlistState.value = UiState.Error(
                    "Failed to clear watchlist: ${e.message}"
                )
                Log.e("WatchListViewModel", "Exception while clearing watchlist", e)
            }
        }
    }

    fun resetAddState() {
        _addToWatchlistState.value = UiState.Idle
    }


    fun resetClearWatchlistState() {
        _clearWatchlistState.value = UiState.Idle
    }

    fun resetRemoveState() {
        _removeFromWatchlistState.value = UiState.Idle
    }

    fun refresh() {
        loadWatchlist()
    }
}
