package com.example.dealspy.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.repo.SearchRepo
import com.example.dealspy.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject
@HiltViewModel
class PriceComparisonViewModel @Inject constructor(
    private val searchRepo: SearchRepo
) : ViewModel() {

    private val _priceComparisonState = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val priceComparisonState = _priceComparisonState.asStateFlow()

    fun loadPriceComparison(productName: String) {
        viewModelScope.launch {
            try {
                _priceComparisonState.value = UiState.Loading
                Log.d("PriceComparisonViewModel", "Loading price comparison for: $productName")

                val response = searchRepo.searchProducts(productName)

                if (response.success && response.data != null) {  // ← Fixed: direct data access
                    val products = response.data  // ← Fixed: no .products
                    _priceComparisonState.value = if (products.isEmpty()) {
                        UiState.NoData
                    } else {
                        UiState.Success(products)
                    }
                    Log.d("PriceComparisonViewModel", "Found ${products.size} products for comparison")
                } else {
                    _priceComparisonState.value = UiState.Error(response.message ?: "No products found")
                }

            } catch (e: UnknownHostException) {
                Log.e("PriceComparisonViewModel", "Network error", e)
                _priceComparisonState.value = UiState.NoInternet
            } catch (e: retrofit2.HttpException) {
                Log.e("PriceComparisonViewModel", "Server error", e)
                _priceComparisonState.value = UiState.ServerError
            } catch (e: Exception) {
                Log.e("PriceComparisonViewModel", "Error loading price comparison", e)
                _priceComparisonState.value = UiState.Error(e.message ?: "Failed to load comparison")
            }
        }
    }

    fun refresh(productName: String) {
        loadPriceComparison(productName)
    }
}
