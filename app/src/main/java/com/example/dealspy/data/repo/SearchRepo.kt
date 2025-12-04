package com.example.dealspy.data.repo

import android.util.Log
import com.example.dealspy.data.model.CustomResponse
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.remote.SearchApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepo @Inject constructor(
    private val searchApi: SearchApi
) {

    suspend fun searchProducts(query: String): CustomResponse<List<Product>> {
        return try {
            Log.d("SearchRepo", "Searching products for query: $query")
            searchApi.searchQuery(query)
        } catch (e: Exception) {
            Log.e("SearchRepo", "Failed to search products", e)
            CustomResponse(
                success = false,
                message = e.message ?: "Failed to search products",
                data = null
            )
        }
    }
}
