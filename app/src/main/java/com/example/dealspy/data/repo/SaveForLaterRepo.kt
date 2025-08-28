package com.example.dealspy.repository

import com.example.dealspy.data.remote.SaveForLaterApi
import com.example.dealspy.data.model.CustomResponse
import com.example.dealspy.data.model.SaveForLater
import com.example.dealspy.data.repo.AuthRepo
import javax.inject.Inject

class SaveForLaterRepo @Inject constructor(
    private val saveForLaterApi: SaveForLaterApi,
    private val authRepo: AuthRepo
) {

    suspend fun getSaveForLater(): CustomResponse<List<SaveForLater>> {
        val token = authRepo.getIdToken()
        return saveForLaterApi.getSaveForLater(token)
    }

    suspend fun addProductToSaveForLater(product: SaveForLater): CustomResponse<Unit> {
        val token = authRepo.getIdToken()
        return saveForLaterApi.postSaveForLater(product, token)
    }

    suspend fun removeProductFromSaveForLater(productName: String): CustomResponse<Unit> {
        val token = authRepo.getIdToken()
        return saveForLaterApi.deleteSaveForLaterItem(productName, token)
    }
}
