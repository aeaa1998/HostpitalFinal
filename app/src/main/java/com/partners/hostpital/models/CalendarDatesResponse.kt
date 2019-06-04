package com.partners.hostpital.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.sql.Date
import java.util.*

class CalendarDatesResponse (@SerializedName("id") val id: Int,
                      @SerializedName("reason") val reason: String,
                      @SerializedName("date") val date: Date,
                      @SerializedName("status") val status: Int,
                      @SerializedName("doctor_id") val doctorId: Int,
                      @SerializedName("patient_id") val patientId: Int,
                      @SerializedName("patient") val patient: SinglePatientResponse,
                      @SerializedName("doctor") val doctor: SingleDoctorResponse,
                      @SerializedName("report") val report: ReportsResource
): Serializable