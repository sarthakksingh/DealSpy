package com.example.dealspy.vm

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.model.SaveForLater
import com.example.dealspy.data.repo.GeminiService
import com.example.dealspy.data.repo.SaveForLaterRepository
import com.example.dealspy.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val geminiService: GeminiService,
    private val saveForLaterRepository: SaveForLaterRepository
) : ViewModel() {

    private val _searchList = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val searchList = _searchList.asStateFlow()

    private val _priceCompareList = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val priceCompareList = _priceCompareList.asStateFlow()

    private val _savedItems = MutableStateFlow<Set<String>>(emptySet())
    val savedItems = _savedItems.asStateFlow()

    private val _saveForLaterState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val saveForLaterState = _saveForLaterState.asStateFlow()

    init {
        loadSavedItems()
    }

    private fun loadSavedItems() {
        viewModelScope.launch {
            try {
                val response = saveForLaterRepository.getSaveForLater()
                if (response.success && response.data != null) {
                    val savedUrls = response.data.map { it.deepLink }.toSet()
                    _savedItems.value = savedUrls
                    Log.d("SearchViewModel", "Loaded ${savedUrls.size} saved items from backend")
                } else {
                    _savedItems.value = emptySet()
                    Log.w("SearchViewModel", "No saved items or error: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error loading saved items", e)
                _savedItems.value = emptySet()
            }
        }
    }

    fun toggleSaveForLater(product: Product) {
        viewModelScope.launch {
            try {
                _saveForLaterState.value = UiState.Loading

                val isCurrentlySaved = _savedItems.value.contains(product.deepLink)

                if (isCurrentlySaved) {
                    // Remove from backend
                    val response = saveForLaterRepository.removeFromSaveForLater(product.name)
                    if (response.success == true) {
                        // Update local state
                        val updatedSet = _savedItems.value.toMutableSet()
                        updatedSet.remove(product.deepLink)
                        _savedItems.value = updatedSet
                        _saveForLaterState.value = UiState.Success("Removed from Save for Later")
                        Log.d("SearchViewModel", "Removed ${product.name} from save for later")
                    } else {
                        _saveForLaterState.value = UiState.Error(response.message ?: "Failed to remove")
                    }
                } else {
                    // Add to backend
                    val saveForLaterItem = SaveForLater(
                        productName = product.name,
                        platformName = product.platformName,
                        lastKnownPrice = product.lastKnownPrice,
                        deepLink = product.deepLink,
                        imageURL = product.imageURL,
                    )

                    val response = saveForLaterRepository.addToSaveForLater(saveForLaterItem)
                    if (response.success) {
                        // Update local state
                        val updatedSet = _savedItems.value.toMutableSet()
                        updatedSet.add(product.deepLink)
                        _savedItems.value = updatedSet
                        _saveForLaterState.value = UiState.Success("Added to Save for Later")
                        Log.d("SearchViewModel", "Added ${product.name} to save for later")
                    } else {
                        _saveForLaterState.value = UiState.Error(response.message ?: "Failed to save")
                    }
                }

            } catch (e: UnknownHostException) {
                _saveForLaterState.value = UiState.NoInternet
                Log.e("SearchViewModel", "No internet connection", e)
            } catch (e: Exception) {
                _saveForLaterState.value = UiState.Error(e.message ?: "Something went wrong")
                Log.e("SearchViewModel", "Error toggling save for later", e)
            }
        }
    }

    fun resetSaveForLaterState() {
        _saveForLaterState.value = UiState.Idle
    }

    fun refreshSavedItems() {
        loadSavedItems()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun searchProductList(productName: String) {
        viewModelScope.launch {
            _searchList.value = UiState.Loading
            try {
                val result = geminiService.generateSearchSuggestions(productName)
                _searchList.value = when {
                    result.isEmpty() -> UiState.NoData
                    else -> UiState.Success(result)
                }
            } catch (e: IOException) {
                _searchList.value = UiState.NoInternet
            } catch (e: HttpException) {
                _searchList.value = UiState.ServerError
            } catch (e: Exception) {
                _searchList.value = UiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun priceCompare(productName: String) {
        viewModelScope.launch {
            _priceCompareList.value = UiState.Loading
            try {
                val result = geminiService.generateSearchSuggestions(productName)
                _priceCompareList.value = when {
                    result.isEmpty() -> UiState.NoData
                    else -> UiState.Success(result)
                }
            } catch (e: IOException) {
                _priceCompareList.value = UiState.NoInternet
            } catch (e: HttpException) {
                _priceCompareList.value = UiState.ServerError
            } catch (e: Exception) {
                _priceCompareList.value = UiState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}
