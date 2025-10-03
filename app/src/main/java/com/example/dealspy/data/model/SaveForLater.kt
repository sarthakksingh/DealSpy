// Create: data/model/SaveForLater.kt
package com.example.dealspy.data.model

import com.google.gson.annotations.SerializedName

data class SaveForLater(
    @SerializedName("productName")
    val productName: String,

    @SerializedName("imageUrl")
    val imageUrl: String?=null,

    @SerializedName("desc")
    val desc: String
)
