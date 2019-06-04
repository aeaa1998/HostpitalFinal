package com.partners.hostpital.models

import com.google.gson.annotations.SerializedName


class SingleDoctorResponse (@SerializedName("recipe") val id: Int,
                             @SerializedName("first_name") val firstName: String,
                             @SerializedName("last_name") val lastName: String,
                             @SerializedName("doctor_type") val doctorType: DoctorTypeResponse,
                             @SerializedName("schedule") val schedules: SchedulesResponse
)