package com.example.dealspy.data.repo

import android.util.Log
import com.example.dealspy.data.model.CustomResponse
import com.example.dealspy.data.remote.AuthApi
import com.example.dealspy.ui.state.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

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

    suspend fun verifyToken(accessToken: String): CustomResponse<Unit> {
        return authApi.verifyToken(accessToken)
    }

    fun isUserSignedIn(): Boolean = auth.currentUser != null

    fun signOut() {
        auth.signOut()
    }
}
