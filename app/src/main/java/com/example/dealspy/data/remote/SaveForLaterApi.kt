package com.example.dealspy.data.remote

import com.example.dealspy.data.model.CustomResponse
import com.example.dealspy.data.model.SaveForLater
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface SaveForLaterApi {

    @GET("saveforLater")
    suspend fun getSaveForLater(
        @Header("Authorization") accessToken: String
    ): CustomResponse<List<SaveForLater>>

    @POST("saveforLater")
    suspend fun postSaveForLater(
        @Body product: SaveForLater,
        @Header("Authorization") accessToken: String
    ): CustomResponse<Unit>

    @DELETE("saveforLater/{productName}")
    suspend fun deleteSaveForLaterItem(
        @Path("productName") productName: String,
        @Header("Authorization") accessToken: String
    ): CustomResponse<Unit>

}