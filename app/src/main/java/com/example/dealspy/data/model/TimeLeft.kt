package com.example.dealspy.data.model

import com.google.gson.annotations.SerializedName

data class TimeLeft(
    @SerializedName("days")
    val days: Long,
    @SerializedName("hours")
    val hours: Int,
    @SerializedName("minutes")
    val min:Int,
    @SerializedName("seconds")
    val sec:Int
)
