package com.example.dealspy.data.repo

import com.example.dealspy.data.model.CustomResponse
import com.example.dealspy.data.model.WatchList
import com.example.dealspy.data.remote.WatchlistApi
import javax.inject.Inject

class WatchListRepo @Inject constructor(
    private val watchlistApi: WatchlistApi,
    private val authRepo: AuthRepo
) {

    suspend fun getWatchlist(): CustomResponse<List<WatchList>> {
        val token = authRepo.getIdToken()
        return watchlistApi.getWatchlist(token)
    }

    suspend fun addProductToWatchlist(product: WatchList): CustomResponse<Unit> {
        val token = authRepo.getIdToken()
        return watchlistApi.postWatchlist(product, token)
    }

    suspend fun removeProductFromWatchlist(productName: String): CustomResponse<Unit> {
        val token = authRepo.getIdToken()
        return watchlistApi.deleteWatchListItem(productName, token)
    }
}
