package com.example.dealspy.data.model

import com.google.gson.annotations.SerializedName


data class UserDetail(
    val watchList: List<WatchList> = emptyList(),
    val saveForLater: List<SaveForLater> = emptyList()
)