package com.partners.hostpital

import android.os.Bundle
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.content_create_user.*

class CreateUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        setSupportActionBar(toolbar)

        add.setOnClickListener {
            val firstName = findViewById<EditText>(R.id.firstname_edit_txt).text.toString()
            val lastName = findViewById<EditText>(R.id.lastname_edit_txt).text.toString()
            val username = findViewById<EditText>(R.id.username_edit_txt).text.toString()
            val email = findViewById<EditText>(R.id.email_edit_txt).text.toString()
            val password = findViewById<EditText>(R.id.password_edit_txt).text.toString()
            val speciality = findViewById<EditText>(R.id.doctor_edit_txt).text.toString()

            doctor_switch.setOnCheckedChangeListener{ _, isChecked ->
                if (isChecked) {
                    if ((firstName == "") || (lastName == "") || (username == "") || (email == "") || (password == "") || (speciality == "")){

                    } else {

                    }

                } else {
                    if ((firstName == "") || (lastName == "") || (username == "") || (email == "") || (password == "")){

                    } else {

                    }
                }
            }

        }
    }

}
