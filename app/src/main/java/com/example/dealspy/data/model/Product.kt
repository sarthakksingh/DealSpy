package com.example.dealspy.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("productName")
    val name: String,

    @SerializedName("brand")
    val brand: String? = null,

    @SerializedName("platform")
    val platformName: String,

    @SerializedName("price")
    val price: String,

    @SerializedName("lastKnownPrice")
    val lastKnownPrice: Double? = null,

    @SerializedName("deepLink")
    val deepLink: String,

    @SerializedName("imageUrl")
    val imageUrl: String? = null
) {

    fun getDiscountPercentage(): String? {
        return if (lastKnownPrice != null && lastKnownPrice > 0) {

            val currentPrice = price.replace(Regex("[^0-9.]"), "").toDoubleOrNull()
            if (currentPrice != null && currentPrice > 0) {
                val discount = ((lastKnownPrice - currentPrice) / lastKnownPrice * 100).toInt()
                if (discount > 0) "$discount% OFF" else null
            } else null
        } else null
    }
}
