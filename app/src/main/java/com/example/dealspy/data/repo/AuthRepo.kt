// Update: data/repo/AuthRepo.kt
package com.example.dealspy.data.repo

import android.util.Log
import com.example.dealspy.data.model.CustomResponse
import com.example.dealspy.data.remote.AuthApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class AuthRepo @Inject constructor(
    private val auth: FirebaseAuth,
    private val authApi: AuthApi
) {
    suspend fun getFCMToken(): String {
        return FirebaseMessaging.getInstance().token.await()
    }
    suspend fun verifyTokenWithFCM(idToken: String, fcmToken: String): CustomResponse<Unit> {
        return authApi.verifyToken("Bearer $idToken", fcmToken)
    }
}
