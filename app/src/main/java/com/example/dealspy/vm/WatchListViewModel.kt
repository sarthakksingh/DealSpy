package com.example.dealspy.vm

import androidx.lifecycle.ViewModel
import com.example.dealspy.data.model.Product
import com.example.dealspy.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class WatchListViewModel @Inject constructor() : ViewModel() {
    private val _watchListState = MutableStateFlow< UiState<List<Product>>>(UiState.Idle)
    val watchListState = _watchListState.asStateFlow()
    fun loadWatchList() {
        _watchListState.value = UiState.Loading
        //Fetch the data from the repository and update the state
    }
    fun onProductClick (product: Product) {

    }
    fun onAddProduct(){

    }
    fun onDeleteProduct(product: Product){

    }
}