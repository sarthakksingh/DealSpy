package com.example.dealspy.data.remote

import com.example.dealspy.data.model.CustomResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthApi {
    @GET("api/auth")
    suspend fun verifyToken(@Query("token") token: String): CustomResponse<Unit>
}