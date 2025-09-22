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

    suspend fun signInWithGoogleCredential(idToken: String): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            Log.d("AuthRepo", "Google sign-in success")
            true
        } catch (e: Exception) {
            Log.e("AuthRepo", "Google sign-in failed: ${e.message}")
            false
        }
    }

    suspend fun getIdToken(): String {
        val currentUser = auth.currentUser
            ?: throw Exception("User not authenticated")

        val idTokenResult = currentUser.getIdToken(true).await()
        val token = idTokenResult.token ?: throw Exception("Token is null")
        Log.d("AuthRepo", "New token: $token")
        return "Bearer $token"
    }

    suspend fun getFCMToken(): String {
        return try {
            val token = FirebaseMessaging.getInstance().token.await()
            Log.d("AuthRepo", "FCM Token retrieved: $token")
            token
        } catch (e: Exception) {
            Log.e("AuthRepo", "Failed to get FCM token", e)
            throw e
        }
    }

    // 🔹 SINGLE METHOD - hits /verify endpoint with FCM token
    suspend fun verifyTokenWithFCM(accessToken: String, fcmToken: String): CustomResponse<Unit> {
        return try {
            Log.d("AuthRepo", "Verifying token with FCM: $fcmToken")
            authApi.verifyToken("Bearer $accessToken", fcmToken)
        } catch (e: Exception) {
            Log.e("AuthRepo", "Token verification failed", e)
            CustomResponse(success = false, message = e.message ?: "Verification failed",null)
        }
    }

    fun isUserSignedIn(): Boolean = auth.currentUser != null

    fun signOut() {
        auth.signOut()
    }
}
