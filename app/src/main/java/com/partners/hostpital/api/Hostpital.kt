package com.partners.hostpital.api

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.partners.hostpital.helpers.Constants
import com.partners.hostpital.models.DoctorResponse
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class Hostpital : Application(){
    companion object {
        @SuppressLint("SimpleDateFormat")
        val newFormat = SimpleDateFormat("HH:mm:ss")
        val newFormatTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val service = API.request()

//        fun <T>getList(response: Call<List<T>>, recycler: RecyclerView, context:Context): List<T> {
//            var r : ArrayList<T>?= null
//            val service = API.request()
//            var boolDone = false
//            while (r == null){
//
//                response.enqueue(object : Callback<List<T>> {
//                override fun onResponse(call: Call<List<T>>, response: Response<List<T>>) {
//
//                    if (response.body() != null && (response.code() < 300)) {
//                        val responseDatabase = requireNotNull(response.body())
//                        r!!.addAll(responseDatabase)
//                        boolDone = true
//                        recycler.adapter?.notifyDataSetChanged()
//
//
//                    } else {
//                        r = arrayListOf()
//
//                        Log.e("fail", response.code().toString() + " - " + response.message())
//                        Toast.makeText(context, response.code().toString() + " - " + response.message(), Toast.LENGTH_LONG).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<List<T>>, t: Throwable) {
//                    r = arrayListOf()
//
//                    Log.e("fail token", t.message)
//
//                }
//            })
//
//
//            }
//            return r!!.toList()
//
//        }

    }
    override fun onCreate() {
        super.onCreate()
        Paper.init(applicationContext)
    }

}