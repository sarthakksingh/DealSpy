package com.example.dealspy.data.remote

import com.example.dealspy.data.model.CustomResponse
import com.example.dealspy.data.model.SaveForLater
import com.example.dealspy.data.model.UserDetail
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface SaveForLaterApi {

    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") accessToken: String
    ): CustomResponse<UserDetail>

    @GET("saveforlater")
    suspend fun getSaveForLater(
        @Header("Authorization") accessToken: String
    ): CustomResponse<List<SaveForLater>>

    @POST("saveforlater")
    suspend fun postSaveForLater(
        @Body product: SaveForLater,
        @Header("Authorization") accessToken: String
    ): CustomResponse<Unit>

    @DELETE("saveforlater/{productName}")
    suspend fun deleteSaveForLaterItem(
        @Path("productName") productName: String,
        @Header("Authorization") accessToken: String
    ): CustomResponse<Unit>

}