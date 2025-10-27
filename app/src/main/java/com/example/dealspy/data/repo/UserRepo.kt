package com.example.dealspy.data.repo

import kotlinx.coroutines.tasks.await


import com.example.dealspy.data.model.CustomResponse
import com.example.dealspy.data.remote.UserApi
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val auth: FirebaseAuth
) {
    suspend fun deleteUser(): CustomResponse<String> {
        return try {
            val token = auth.currentUser?.getIdToken(false)?.await()?.token
            if (token == null) {
                return CustomResponse(
                    success = false,
                    message = "User not authenticated",
                    data = null
                )
            }
            userApi.deleteUser("Bearer $token")
        } catch (e: Exception) {
            CustomResponse(
                success = false,
                message = e.message ?: "Failed to delete account",
                data = null
            )
        }
    }
}
