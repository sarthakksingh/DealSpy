package com.example.dealspy.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("productName")
    val name: String?=null,

    @SerializedName("brand")
    val brand: String? = null,

    @SerializedName("platform")
    val platformName: String?=null,

    @SerializedName("price")
    val price: Double? = null,

//    @SerializedName("priceValue")
//    val priceValue: Double? = null,

    @SerializedName("lastKnownPrice")
    val lastKnownPrice: Double? = null,

    @SerializedName("deepLink")
    val deepLink: String,

    @SerializedName("imageUrl")
    val imageUrl: String? = null
) {
    fun getDiscountPercentage(): String? {
        val currentPrice = price
        return if (lastKnownPrice != null && lastKnownPrice > 0 && currentPrice != null && currentPrice > 0) {
            val discount = ((lastKnownPrice - currentPrice) / lastKnownPrice * 100).toInt()
            if (discount > 0) "$discount% OFF" else null
        } else {
            null
        }
    }
}
