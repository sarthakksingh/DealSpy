package com.example.dealspy.data.model

import com.google.gson.annotations.SerializedName

data class GeminiSearchResponse(
    @SerializedName("product")
    val product: List<Product>
)