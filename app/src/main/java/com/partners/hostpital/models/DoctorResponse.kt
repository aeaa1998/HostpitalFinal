package com.partners.hostpital.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class DoctorResponse (

        @SerializedName("id") val id: Int,
        @SerializedName("first_name") val first_name: String,
                     @SerializedName("last_name") val lastName: String,
                     @SerializedName("doctor_type") val doctorType: DoctorTypeResponse,
                     @SerializedName("dates") val dates: List<CalendarDatesResponse>,
                     @SerializedName("patients") val patients: List<SinglePatientResponse>,
                     @SerializedName("schedule") val schedule: SchedulesResponse,
                     @SerializedName("latitud") val latitud: Double,
                     @SerializedName("longitud") val longitud: Double
): Serializable