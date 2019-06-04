package com.partners.hostpital.models

import com.google.gson.annotations.SerializedName
import java.util.*

class ReportsResource (
                            @SerializedName("id") val  id: Int,
                             @SerializedName("recipe") val recipe: String,
                             @SerializedName("observations") val observations: String
)