package com.example.dealspy.data.model

data class UiProduct(
    val product: Product,
    val brand: String?=null,
    val timeLeftMillis: Long
) {
    val discountPercent: Int
        get() {
            val oldPrice = product.lastKnownPrice
            val currentPrice = product.price
            return if (oldPrice > 0) ((oldPrice - currentPrice) * 100 / oldPrice) else 0
        }
}