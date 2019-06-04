package com.partners.hostpital.models

import com.google.gson.annotations.SerializedName
import java.sql.Time
import java.util.*


class SchedulesResponse (@SerializedName("name") val id: String,
                         @SerializedName("enter_time") val enterTime: Date,
                         @SerializedName("exit_time") val exitTime: Date
)