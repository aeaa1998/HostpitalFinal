package com.partners.hostpital.api

import android.database.Observable
import com.partners.hostpital.models.*
import com.partners.hostpital.requests.UpdateDateRequest
import com.partners.hostpital.requests.UpdateUserInfoRequest
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET
import java.util.*


interface ApiHostpital{
    //Login devuelve token
    @POST("get/token")
    @FormUrlEncoded
    fun oauthToken(
            @Field("grant_type") grantType: String,
            @Field("client_id") clientId: Int,
            @Field("client_secret") clientSecret: String,
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("scope") scope: String
    ): Call<TokenResponse>

    @POST("dates")
    @FormUrlEncoded
    fun postDate(
            @Field("doctor_id") doctorId: Int,
            @Field("patient_id") patientId: Int,
            @Field("date") date: String,
            @Field("reason") reason: String,
            @Field("is_doctor") password: Int
    ): Call<CalendarDatesResponse?>
    @POST("users/")
    @FormUrlEncoded
    fun newUser(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("email") email: String,
            @Field("first_name") firstName: String,
            @Field("last_name") lastName: String,
            @Field("doctor_type") doctorType: String,
            @Field("is_doctor") isDoctor: Int
            ): Call<UserResponse>


    @GET("dates/{user_id}/{is_doctor}")
    fun datesByDate(@Path("user_id") userId: Int, @Path("is_doctor") doctorId: Int): Call<List<CalendarDatesResponse>>

    @GET("pending/dates/{user_id}/1")
    fun pendingDates(@Path("user_id") userId: Int): Call<List<CalendarDatesResponse>>

    @GET("all/dates/{user_id}/{is_doctor}")
    fun processedDates(@Path("user_id") userId: Int, @Path("is_doctor") doctorId: Int): Call<List<CalendarDatesResponse>>

    @GET("all/doctors/{user_id}/{is_doctor}")
    fun allDoctors(@Path("user_id") userId: Int, @Path("is_doctor") doctorId: Int): Call<List<DoctorResponse>>

    @GET("all/patients/doctor/{user_id}/1}")
    fun allPatientsDoctor(@Path("user_id") userId: Int): Call<List<PatientResponse>>

    @GET("/doctors/{id}}")
    fun getDoctoryById(@Path("id") Id: Int): Call<DoctorResponse>



    @PUT("dates/{id}")
    fun processDate(
            @Path("id") dateId: Int,
            @Body updateDateRequest: UpdateDateRequest
            ): Call<CalendarDatesResponse>

    @PUT("users/{id}")
    fun updateInfo(
            @Path("id") userId: Int,
            @Body updateDateRequest: UpdateUserInfoRequest
            ): Call<UserResponse>


}