package com.partners.hostpital.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object API {
    val url = "https://2da1f0f3.ngrok.io"
    fun request(): ApiHostpital{
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        val gsonTime = GsonBuilder().setDateFormat("HH:mm:ss").create()
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson)).addConverterFactory(GsonConverterFactory.create(gsonTime)).baseUrl("$url/api/").build()
        return retrofit.create(ApiHostpital::class.java)
    }


}
