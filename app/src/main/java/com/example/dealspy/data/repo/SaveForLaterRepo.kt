package com.example.dealspy.data.repo

import android.util.Log
import com.example.dealspy.data.model.CustomResponse
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.model.UserDetail
import com.example.dealspy.data.remote.SaveForLaterApi
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveForLaterRepository @Inject constructor(
    private val saveForLaterApi: SaveForLaterApi,
    private val auth: FirebaseAuth
) {

    private suspend fun getAuthToken(): String {
        val currentUser = auth.currentUser ?: throw Exception("User not authenticated")
        val idTokenResult = currentUser.getIdToken(true).await()
        val token = idTokenResult.token ?: throw Exception("Token is null")
        return "Bearer $token"
    }

    suspend fun getUserProfile(): CustomResponse<UserDetail> {
        return try {
            val token = getAuthToken()
            Log.d("SaveForLaterRepository", "Fetching user profile...")
            saveForLaterApi.getProfile(token)
        } catch (e: Exception) {
            Log.e("SaveForLaterRepository", "Failed to get user profile", e)
            CustomResponse(
                success = false,
                message = e.message ?: "Failed to fetch user profile",
                data = null
            )
        }
    }

    suspend fun getSaveForLater(): CustomResponse<List<Product>> {
        return try {
            val token = getAuthToken()
            Log.d("SaveForLaterRepository", "Fetching save for later items...")
            saveForLaterApi.getSaveForLater(token)
        } catch (e: Exception) {
            Log.e("SaveForLaterRepository", "Failed to get save for later", e)
            CustomResponse(
                success = false,
                message = e.message ?: "Failed to fetch save for later",
                data = emptyList()
            )
        }
    }

    suspend fun addToSaveForLater(product: Product): CustomResponse<Product> {
        return try {
            val token = getAuthToken()
            Log.d("SaveForLaterRepository", "Adding to save for later: ${product.name}")
            saveForLaterApi.postSaveForLater(product, token)
        } catch (e: Exception) {
            Log.e("SaveForLaterRepository", "Failed to add to save for later", e)
            CustomResponse(
                success = false,
                message = e.message ?: "Failed to add to save for later",
                data = null
            )
        }
    }

    suspend fun removeFromSaveForLater(productName: String): CustomResponse<Unit> {
        return try {
            val token = getAuthToken()
            Log.d("SaveForLaterRepository", "Removing from save for later: $productName")
            saveForLaterApi.deleteSaveForLaterItem(productName, token)
        } catch (e: Exception) {
            Log.e("SaveForLaterRepository", "Failed to remove from save for later", e)
            CustomResponse(
                success = false,
                message = e.message ?: "Failed to remove from save for later",
                data = null
            )
        }
    }
}
