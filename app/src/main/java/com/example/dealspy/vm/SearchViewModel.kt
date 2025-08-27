package com.example.dealspy.vm

import android.net.http.HttpException
import android.os.Build
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
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _searchList = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val searchList = _searchList.asStateFlow()

    private val _priceCompareList = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val priceCompareList = _priceCompareList.asStateFlow()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun searchProductList(productName: String) {
        viewModelScope.launch {
            _searchList.value = UiState.Loading
            try {
                val result = GeminiService.generateSearchSuggestions(productName)
                _searchList.value = when {
                    result.isEmpty() -> UiState.NoData
                    else -> UiState.Success(result)
                }
            } catch (e: IOException) {
                _searchList.value = UiState.NoInternet
            } catch (e: HttpException) {
                _searchList.value = UiState.ServerError
            } catch (e: Exception) {
                _searchList.value = UiState.Failed(e.message ?: "Something went wrong")
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun priceCompare(productName: String) {
        viewModelScope.launch {
            _priceCompareList.value = UiState.Loading
            try {
                val result = GeminiService.generateSearchSuggestions(productName)
                _priceCompareList.value = when {
                    result.isEmpty() -> UiState.NoData
                    else -> UiState.Success(result)
                }
            } catch (e: IOException) {
                _priceCompareList.value = UiState.NoInternet
            } catch (e: HttpException) {
                _priceCompareList.value = UiState.ServerError
            } catch (e: Exception) {
                _priceCompareList.value = UiState.Failed(e.message ?: "Something went wrong")
            }
        }
    }
}
