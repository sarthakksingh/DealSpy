package com.example.dealspy.vm



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.repo.GeminiService
import com.example.dealspy.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PriceComparisonViewModel @Inject constructor(
    private val geminiService: GeminiService
) : ViewModel() {

    private val _priceComparisonState = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val priceComparisonState = _priceComparisonState.asStateFlow()

    private val _productName = MutableStateFlow("")
    val productName = _productName.asStateFlow()

    fun loadPriceComparison(productName: String) {
        _productName.value = productName
        viewModelScope.launch {
            _priceComparisonState.value = UiState.Loading
            try {
                val result = geminiService.generatePriceComparison(productName)
                _priceComparisonState.value = when {
                    result.isEmpty() -> UiState.NoData
                    else -> UiState.Success(result)
                }
            } catch (e: IOException) {
                _priceComparisonState.value = UiState.NoInternet
            } catch (e: Exception) {
                _priceComparisonState.value = UiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun retryPriceComparison() {
        if (_productName.value.isNotBlank()) {
            loadPriceComparison(_productName.value)
        }
    }
}
