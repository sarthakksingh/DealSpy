package com.example.dealspy.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.model.UiProduct
import com.example.dealspy.data.model.WatchList
import com.example.dealspy.data.repo.WatchlistRepository
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
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

    private val _watchListState = MutableStateFlow<UiState<List<UiProduct>>>(UiState.Idle)
    val watchListState = _watchListState.asStateFlow()

    private val _addToWatchlistState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val addToWatchlistState = _addToWatchlistState.asStateFlow()

    private val _removeFromWatchlistState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val removeFromWatchlistState = _removeFromWatchlistState.asStateFlow()

    init {
        loadWatchlist()
    }

    fun loadWatchlist() {
        viewModelScope.launch {
            try {
                _watchListState.value = UiState.Loading
                Log.d("WatchListViewModel", "Loading watchlist...")

                val response = watchlistRepository.getWatchlist()

                if (response.success == true && response.data != null) {
                    Log.d("WatchListViewModel", "Watchlist loaded: ${response.data.size} items")

                    // 🔹 CONVERT WatchList (DTO) → Product → UiProduct
                    val uiProducts = response.data.map { watchlistItem ->
                        // Convert WatchlistDTO to Product
                        val product = Product(
                            name = watchlistItem.productName,
                            platformName = extractPlatformFromDesc(watchlistItem.desc),
                            priceRaw = extractPriceFromDesc(watchlistItem.desc),
                            deepLink = "", // Extract from desc if available
                            imageURL = watchlistItem.imageUrl,
                            discount = null,
                            lastKnownPrice = 0
                        )

                        // Convert to UiProduct with time left calculation
                        UiProduct(
                            product = product,
                            brand = extractPlatformFromDesc(watchlistItem.desc),
                            timeLeftMillis = calculateTimeLeft(watchlistItem.watchEndDateParsed)
                        )
                    }

                    _watchListState.value = if (uiProducts.isEmpty()) {
                        UiState.NoData
                    } else {
                        UiState.Success(uiProducts)
                    }
                } else {
                    _watchListState.value = UiState.Error(response.message ?: "Failed to load watchlist")
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

                // 🔹 CONVERT Product → WatchlistDTO
                val watchlistItem = WatchList(
                    productName = product.name,
                    watchEndDate = watchEndDate?.toString() ?: LocalDate.now().plusDays(30).toString(),
                    imageUrl = product.imageURL,
                    desc = createDescFromProduct(product)
                )

                val response = watchlistRepository.addToWatchlist(watchlistItem)

                if (response.success == true) {
                    Log.d("WatchListViewModel", "Added to watchlist successfully")
                    _addToWatchlistState.value = UiState.Success(Unit)
                    loadWatchlist() // Refresh
                } else {
                    _addToWatchlistState.value = UiState.Error(response.message ?: "Failed to add to watchlist")
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

                val response = watchlistRepository.removeFromWatchlist(productName)

                if (response.success == true) {
                    Log.d("WatchListViewModel", "Removed from watchlist successfully")
                    _removeFromWatchlistState.value = UiState.Success(Unit)
                    loadWatchlist() // Refresh
                } else {
                    _removeFromWatchlistState.value = UiState.Error(response.message ?: "Failed to remove from watchlist")
                }
            } catch (e: Exception) {
                Log.e("WatchListViewModel", "Error removing from watchlist", e)
                _removeFromWatchlistState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    // 🔹 YOUR EXISTING METHODS
    fun onProductClick(product: Product) {
        Log.d("WatchListViewModel", "Product clicked: ${product.name}")
        // Handle navigation or open deep link
    }

    fun onAddProduct() {
        Log.d("WatchListViewModel", "Add product clicked")
        // Handle navigation to add product screen
    }

    fun onDeleteProduct(product: Product) {
        removeFromWatchlist(product.name)
    }

    // 🔹 HELPER METHODS
    private fun calculateTimeLeft(watchEndDate: LocalDate?): Long {
        return if (watchEndDate != null) {
            val endTime = watchEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val currentTime = System.currentTimeMillis()
            maxOf(0L, endTime - currentTime)
        } else {
            // Default: 30 days
            30 * 24 * 60 * 60 * 1000L
        }
    }

    private fun extractPlatformFromDesc(desc: String): String {
        // Parse platform name from description
        // Example: "Amazon - Electronics" -> "Amazon"
        return desc.substringBefore(" - ").takeIf { it.isNotBlank() } ?: "Unknown"
    }

    private fun extractPriceFromDesc(desc: String): String {
        // Try to extract price from description
        // You can implement regex to find price patterns
        val priceRegex = "₹[\\d,]+".toRegex()
        return priceRegex.find(desc)?.value ?: "₹0"
    }

    private fun createDescFromProduct(product: Product): String {
        // Create description from product info
        return "${product.platformName} - ${product.priceRaw} - ${product.name}"
    }

    fun resetAddState() {
        _addToWatchlistState.value = UiState.Idle
    }

    fun resetRemoveState() {
        _removeFromWatchlistState.value = UiState.Idle
    }

    fun refresh() {
        loadWatchlist()
    }
}
