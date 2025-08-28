package com.example.dealspy.data.model

import com.google.gson.annotations.SerializedName

data class WatchList(
    @SerializedName("productName")
    val productName: String,
    @SerializedName("timeLeft")
    val timeLeft: TimeLeft? = null,
    @SerializedName("watchEndDate")
    val watchEndDate: String? = null
)