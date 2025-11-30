package com.example.dealspy.vm

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.repo.GeminiService
import com.example.dealspy.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class PriceComparisonViewModel @Inject constructor(
    private val geminiService: GeminiService
) : ViewModel() {

    private val _priceComparisonState = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val priceComparisonState = _priceComparisonState.asStateFlow()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun loadPriceComparison(productName: String) {
        viewModelScope.launch {
            try {
                _priceComparisonState.value = UiState.Loading
                Log.d("PriceComparisonViewModel", "Loading price comparison for: $productName")

                val response = geminiService.generatePriceComparison(productName)

                if (response.isEmpty()) {
                    _priceComparisonState.value = UiState.NoData
                } else {
                    _priceComparisonState.value = UiState.Success(response)
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

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun refresh(productName: String) {
        loadPriceComparison(productName)
    }
}
