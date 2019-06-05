package com.partners.hostpital.models

import com.google.gson.annotations.SerializedName


class UserResponse (
        @SerializedName("id") val id: Int,
        @SerializedName("first_name") val firstName: String,
        @SerializedName("last_name") val lastName: String,
        @SerializedName("doctor") val doctor: DoctorResponse,
        @SerializedName("patient") val patient: PatientResponse
)
