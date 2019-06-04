package com.partners.hostpital.models

import com.google.gson.annotations.SerializedName

class DoctorTypeResponse (
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String
)