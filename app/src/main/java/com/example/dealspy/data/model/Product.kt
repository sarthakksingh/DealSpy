package com.example.dealspy.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("name")
    val name: String,

    @SerializedName("platform")
    val platformName: String,

    @SerializedName("price")
    val priceRaw: String,

    @SerializedName("deepLink")
    val deepLink: String,

    @SerializedName("imageUrl")
    val imageURL: String,

    @SerializedName("discount")
    val discount: String? = null,

    // Keep your existing computed property
    val lastKnownPrice: Int = 0
) {
    val price: Int
        get() = priceRaw.replace("₹", "").replace(",", "").toIntOrNull() ?: 0
}