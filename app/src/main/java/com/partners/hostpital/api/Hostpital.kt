package com.partners.hostpital.api

import android.app.Application
import io.paperdb.Paper
import java.text.SimpleDateFormat

class Hostpital : Application(){
    companion object {
        val newFormat = SimpleDateFormat("HH:mm:ss")

    }
    override fun onCreate() {
        super.onCreate()
        Paper.init(applicationContext)
    }
}