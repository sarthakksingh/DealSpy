package com.example.dealspy.data.model

import com.google.gson.annotations.SerializedName

data class SaveForLater(
    @SerializedName("productName")
    val productName: String
)
