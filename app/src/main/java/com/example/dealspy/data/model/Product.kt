package com.example.dealspy.data.model
import com.google.gson.annotations.SerializedName // <-- Add this import

data class Product(
    @SerializedName("name")
    val name: String,

    @SerializedName("platform name")
    val platform_name: String,

    @SerializedName("price")
    val price: String,

    @SerializedName("deep link")
    val deep_link: String,

    @SerializedName("image URL")
    val image_URL: String
)