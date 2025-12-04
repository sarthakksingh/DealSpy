package com.example.dealspy.data.remote

import com.example.dealspy.data.model.CustomResponse
import com.example.dealspy.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("micro/v1/search")
    suspend fun searchQuery(
        @Query("q") product: String
    ): CustomResponse<List<Product>>

}