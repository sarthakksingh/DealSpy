package com.example.dealspy.data.remote

import com.example.dealspy.data.model.CustomResponse
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.model.WatchList
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface WatchlistApi {
    @GET("watchlist")
    suspend fun getWatchlist(
        @Header("Authorization") accessToken:String
    ): CustomResponse<List<WatchList>>

    @POST("watchlist")
    suspend fun postWatchlist(
        @Body product: WatchList,
        @Header("Authorization") accessToken:String
    ): CustomResponse<Unit>

    @DELETE("watchlist/{productName}")
    suspend fun deleteWatchListItem(
        @Path("productName") productName: String,
        @Header("Authorization") accessToken:String
    ): CustomResponse<Unit>



}