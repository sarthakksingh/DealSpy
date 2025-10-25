package com.example.dealspy.data.remote

import com.example.dealspy.data.model.CustomResponse
import retrofit2.http.DELETE
import retrofit2.http.Header


interface UserApi {
    @DELETE("users/delete")
    suspend fun deleteUser(
        @Header("Authorization") token: String
    ): CustomResponse<String>
}
