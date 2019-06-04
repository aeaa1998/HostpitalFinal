package com.partners.hostpital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ChooseApp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_app)
    }
    fun choosePatient(v: View){
        val i = Intent(this, PatientActivity::class.java)
        startActivity(i)
    }
    fun chooseDoctor(v: View){
        val i = Intent(this, DoctorActivity::class.java)
        startActivity(i)
    }

}
