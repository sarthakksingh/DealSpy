package com.example.dealspy.data.model
import com.google.gson.annotations.SerializedName // <-- Add this import

data class Product(
    @SerializedName("name")
    val name: String,

    @SerializedName("platform name")
    val platformName: String,

    @SerializedName("price")
    val price: Int,

    @SerializedName("has price dropped")
    val lastKnownPrice: Int,

    @SerializedName("deep link")
    val deepLink: String,

    @SerializedName("image URL")
    val imageURL: String
)