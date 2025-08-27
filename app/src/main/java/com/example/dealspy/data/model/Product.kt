package com.example.dealspy.data.model
import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("name")
    val name: String,

    @SerializedName("platform name")
    val platformName: String,

    @SerializedName("price")
    val priceRaw: String,

    @SerializedName("last known price") 
    val lastKnownPrice: Int,

    @SerializedName("deep link")
    val deepLink: String,

    @SerializedName("image URL")
    val imageURL: String
)
{
    val price: Int
        get() = priceRaw.replace("₹", "").toIntOrNull() ?: 0
}