package com.example.dealspy.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.repo.GeminiService
import com.example.dealspy.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel: ViewModel() {

    private val _searchList = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val searchList = _searchList.asStateFlow()

    private val _priceCompareList = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val priceCompareList = _priceCompareList.asStateFlow()


    fun searchProductList(productName: String) {
        viewModelScope.launch {
            _searchList.value = UiState.Loading
            try {
                val product = GeminiService.generateSearchSuggestions(productName)
                _searchList.value = if (product.isEmpty()) {
                    UiState.Failed("No products found for $productName")
                } else {
                    UiState.Success(product)
                }
            } catch (e: Exception) {
                _searchList.value = UiState.Failed(e.message ?: "Unknown error occurred")
            }
        }
    }
    fun priceCompare(productName: String){
        viewModelScope.launch {
            _priceCompareList.value = UiState.Loading
            try {
                val product = GeminiService.generateSearchSuggestions(productName)
                _priceCompareList.value = if (product.isEmpty()) {
                    UiState.Failed("No products found for $productName")
                } else {
                    UiState.Success(product)
                }
            } catch (e: Exception) {
                _searchList.value = UiState.Failed(e.message ?: "Unknown error occurred")
            }
        }
    }

}