package com.example.dealspy.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.repo.WatchlistRepository
import com.example.dealspy.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class WatchListViewModel @Inject constructor(
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

    private val _watchlist = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val watchlist = _watchlist.asStateFlow()

    private val _addToWatchlistState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val addToWatchlistState = _addToWatchlistState.asStateFlow()

    private val _removeFromWatchlistState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val removeFromWatchlistState = _removeFromWatchlistState.asStateFlow()

    init {
        getWatchlistProducts()
    }

    fun getWatchlistProducts() {
        viewModelScope.launch {
            try {
                _watchlist.value = UiState.Loading
                Log.d("WatchListViewModel", "Loading watchlist...")
                val response = watchlistRepository.getWatchlist()
                if (response.success && response.data != null) {
                    Log.d("WatchListViewModel", "Watchlist loaded: ${response.data.size} items")
                    _watchlist.value = if (response.data.isEmpty()) {
                        UiState.NoData
                    } else {
                        UiState.Success(response.data)
                    }
                } else {
                    _watchlist.value = UiState.Error(response.message ?: "Failed to load watchlist")
                }
            } catch (e: UnknownHostException) {
                Log.e("WatchListViewModel", "Network error", e)
                _watchlist.value = UiState.NoInternet
            } catch (e: retrofit2.HttpException) {
                Log.e("WatchListViewModel", "Server error", e)
                _watchlist.value = UiState.ServerError
            } catch (e: Exception) {
                Log.e("WatchListViewModel", "Error loading watchlist", e)
                _watchlist.value = UiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun addToWatchlist(product: Product) {
        viewModelScope.launch {
            try {
                _addToWatchlistState.value = UiState.Loading
                Log.d("WatchListViewModel", "Adding to watchlist: ${product.name}")
                val response = watchlistRepository.addToWatchlist(product)
                if (response.success) {
                    Log.d("WatchListViewModel", "Added to watchlist successfully")
                    _addToWatchlistState.value = UiState.Success("Added to watchlist!")
                    getWatchlistProducts()
                } else {
                    _addToWatchlistState.value = UiState.Error(response.message ?: "Failed to add to watchlist")
                }
            } catch (e: UnknownHostException) {
                Log.e("WatchListViewModel", "Network error", e)
                _addToWatchlistState.value = UiState.NoInternet
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
                val response = watchlistRepository.removeFromWatchlist(productName)
                if (response.success) {
                    Log.d("WatchListViewModel", "Removed from watchlist successfully")
                    _removeFromWatchlistState.value = UiState.Success("Removed from watchlist")
                    getWatchlistProducts()
                } else {
                    _removeFromWatchlistState.value = UiState.Error(response.message ?: "Failed to remove from watchlist")
                }
            } catch (e: UnknownHostException) {
                Log.e("WatchListViewModel", "Network error", e)
                _removeFromWatchlistState.value = UiState.NoInternet
            } catch (e: Exception) {
                Log.e("WatchListViewModel", "Error removing from watchlist", e)
                _removeFromWatchlistState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun resetAddState() {
        _addToWatchlistState.value = UiState.Idle
    }

    fun resetRemoveState() {
        _removeFromWatchlistState.value = UiState.Idle
    }

    fun refresh() {
        getWatchlistProducts()
    }
}
