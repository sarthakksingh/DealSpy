// Update: data/repo/WatchlistRepository.kt
package com.example.dealspy.data.repo

import android.util.Log
import com.example.dealspy.data.model.CustomResponse
import com.example.dealspy.data.model.WatchList
import com.example.dealspy.data.remote.WatchlistApi
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchlistRepository @Inject constructor(
    private val watchlistApi: WatchlistApi,
    private val auth: FirebaseAuth
) {

    private suspend fun getAuthToken(): String {
        val currentUser = auth.currentUser ?: throw Exception("User not authenticated")
        val idTokenResult = currentUser.getIdToken(true).await()
        val token = idTokenResult.token ?: throw Exception("Token is null")
        return "Bearer $token"
    }

    suspend fun getWatchlist(): CustomResponse<List<WatchList>> {
        return try {
            val token = getAuthToken()
            Log.d("WatchlistRepository", "Fetching watchlist...")
            watchlistApi.getWatchlist(token)
        } catch (e: Exception) {
            Log.e("WatchlistRepository", "Failed to get watchlist", e)
            CustomResponse(
                success = false,
                message = e.message ?: "Failed to fetch watchlist",
                data = emptyList()
            )
        }
    }

    suspend fun addToWatchlist(watchlistItem: WatchList): CustomResponse<Unit> {
        return try {
            val token = getAuthToken()
            Log.d("WatchlistRepository", "Adding to watchlist: ${watchlistItem.productName}")
            watchlistApi.postWatchlist(watchlistItem, token)
        } catch (e: Exception) {
            Log.e("WatchlistRepository", "Failed to add to watchlist", e)
            CustomResponse(
                success = false,
                message = e.message ?: "Failed to add to watchlist",
                null
            )
        }
    }

    suspend fun removeFromWatchlist(productName: String): CustomResponse<Unit> {
        return try {
            val token = getAuthToken()
            Log.d("WatchlistRepository", "Removing from watchlist: $productName")
            watchlistApi.deleteWatchListItem(productName, token)
        } catch (e: Exception) {
            Log.e("WatchlistRepository", "Failed to remove from watchlist", e)
            CustomResponse(
                success = false,
                message = e.message ?: "Failed to remove from watchlist",
                null
            )
        }
    }
}
