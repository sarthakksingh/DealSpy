// Update: data/remote/WatchlistApi.kt
package com.example.dealspy.data.remote

import com.example.dealspy.data.model.CustomResponse
import com.example.dealspy.data.model.WatchList
import retrofit2.http.*

interface WatchlistApi {

    @GET("watchlist")
    suspend fun getWatchlist(
        @Header("Authorization") accessToken: String
    ): CustomResponse<List<WatchList>>

    @POST("watchlist")
    suspend fun postWatchlist(
        @Body watchlistItem: WatchList,
        @Header("Authorization") accessToken: String
    ): CustomResponse<Unit>

    @DELETE("watchlist/{productName}")
    suspend fun deleteWatchListItem(
        @Path("productName") productName: String,
        @Header("Authorization") accessToken: String
    ): CustomResponse<Unit>
}
