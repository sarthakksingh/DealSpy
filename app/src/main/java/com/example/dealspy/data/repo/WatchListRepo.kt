// Update: data/repo/WatchListRepo.kt
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
class WatchListRepo @Inject constructor(
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
            Log.d("WatchListRepo", "Fetching watchlist...")
            watchlistApi.getWatchlist(token)
        } catch (e: Exception) {
            Log.e("WatchListRepo", "Failed to get watchlist", e)
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
            Log.d("WatchListRepo", "Adding to watchlist: ${watchlistItem.productName}")
            watchlistApi.postWatchlist(watchlistItem, token)
        } catch (e: Exception) {
            Log.e("WatchListRepo", "Failed to add to watchlist", e)
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
            Log.d("WatchListRepo", "Removing from watchlist: $productName")
            watchlistApi.deleteWatchListItem(productName, token)
        } catch (e: Exception) {
            Log.e("WatchListRepo", "Failed to remove from watchlist", e)
            CustomResponse(
                success = false,
                message = e.message ?: "Failed to remove from watchlist",
                null
            )
        }
    }

    suspend fun clearAllWatchlist(): CustomResponse<String> {
        return try {
            val token = getAuthToken()
            Log.d("WatchListRepo", "Clearing all watchlist items...")

            // Call the API endpoint
            val response = watchlistApi.clearAllWatchlist(token)

            if (response.success) {
                Log.d("WatchListRepo", "Successfully cleared all watchlist items")
                CustomResponse(
                    success = true,
                    message = response.message ?: "All watchlist items cleared successfully",
                    data = response.data ?: "Watchlist emptied"
                )
            } else {
                Log.e("WatchListRepo", "Failed to clear watchlist: ${response.message}")
                CustomResponse(
                    success = false,
                    message = response.message ?: "Failed to clear watchlist",
                    data = null
                )
            }

        } catch (e: Exception) {
            Log.e("WatchListRepo", "Exception while clearing watchlist", e)
            CustomResponse(
                success = false,
                message = e.message ?: "Failed to clear watchlist",
                data = null
            )
        }
    }
}
