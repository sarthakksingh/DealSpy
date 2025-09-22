// Update: data/remote/AuthApi.kt
package com.example.dealspy.data.remote

import com.example.dealspy.data.model.CustomResponse
import retrofit2.http.*

interface AuthApi {

    @GET("auth/verify")
    suspend fun verifyToken(
        @Header("Authorization") token: String,
        @Header("X-FCM-TOKEN") fcmToken: String
    ): CustomResponse<Unit>
}
