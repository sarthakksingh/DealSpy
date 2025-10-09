
package com.example.dealspy.data.model

import com.google.gson.annotations.SerializedName


data class SaveForLater(
    @SerializedName("productName")
    val productName: String,

    @SerializedName("platformName")
    val platformName: String,

    @SerializedName("lastKnownPrice")
    val lastKnownPrice: Int,

    @SerializedName("deepLink")
    val deepLink: String,

    @SerializedName("imageURL")
    val imageURL: String?=null,

)

