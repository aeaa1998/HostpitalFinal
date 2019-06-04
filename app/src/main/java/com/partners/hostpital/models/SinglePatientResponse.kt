package com.partners.hostpital.models

import com.google.gson.annotations.SerializedName
import java.util.*


class SinglePatientResponse (@SerializedName("id") val id: Int,
                             @SerializedName("first_name") val firstName: String,
                             @SerializedName("last_name") val lastName: String,
                             @SerializedName("email") val email: String
)