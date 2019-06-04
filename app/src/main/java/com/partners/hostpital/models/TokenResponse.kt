package com.partners.hostpital.models

import com.google.gson.annotations.SerializedName

class TokenResponse (
                     @SerializedName("id") val id: Int,
                     @SerializedName("access_token") val accessToken: String,
                     @SerializedName("first_name") val firstName: String,
                     @SerializedName("last_name") val lastName: String,
                     @SerializedName("doctor_id") val doctorId: Int,
                     @SerializedName("patient_id") val patientId: Int,
                     @SerializedName("is_doctor") val isDoctor: Int = 0)