package com.partners.hostpital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.partners.hostpital.helpers.Constants
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bool = Paper.book().read<String>(Constants.accessToken) ?: "" !== ""
//        Toast.makeText(this, Paper.book().read<String>(Constants.accessToken), Toast.LENGTH_LONG).show()
        if (!bool){
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
        }else{
            val booleanDoctor = Paper.book().read<Int>(Constants.isDoctor) == 1

            val intentActivity = if (booleanDoctor)
            {
                Intent(this@SplashActivity, ChooseApp::class.java)
            }else{
                Intent(this@SplashActivity, PatientActivity::class.java)
            }
            startActivity(intentActivity)
        }
    }


}
