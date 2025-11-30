package com.example.dealspy.data.model

data class UserDetail(
    val watchList: List<Product> = emptyList(),
    val saveForLater: List<Product> = emptyList()
)
