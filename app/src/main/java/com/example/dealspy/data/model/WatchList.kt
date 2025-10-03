// Update: data/model/WatchList.kt
package com.example.dealspy.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class WatchList(
    @SerializedName("productName")
    val productName: String,

    @SerializedName("watchEndDate")
    val watchEndDate: String?, // ISO date string from backend

    @SerializedName("imageUrl")
    val imageUrl: String?,

    @SerializedName("desc")
    val desc: String
) {
    val watchEndDateParsed: LocalDate?
        get() = try {
            if (watchEndDate != null) LocalDate.parse(watchEndDate) else null
        } catch (e: Exception) {
            null
        }
}
