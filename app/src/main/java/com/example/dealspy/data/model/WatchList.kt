package com.example.dealspy.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class WatchList(
    @SerializedName("productName")
    val productName: String,

    @SerializedName("watchEndDate")
    val watchEndDate: String?,

    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("deepLink")
    val deepLink:String
) {
    val watchEndDateParsed: LocalDate?
        get() = try {
            if (watchEndDate != null) LocalDate.parse(watchEndDate) else null
        } catch (e: Exception) {
            null
        }
}
