package com.partners.hostpital

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.partners.hostpital.api.Hostpital
import com.partners.hostpital.models.UserResponse
import io.paperdb.Paper

import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.content_create_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field

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
            val doctorSwitch = findViewById<Switch>(R.id.doctor_switch)
            doctorSwitch.setOnCheckedChangeListener{ _, isChecked ->
                if (isChecked) {
                    if ((firstName == "") || (lastName == "") || (username == "") || (email == "") || (password == "") || (speciality == "")){
                        Toast.makeText(applicationContext, "Llene todos los campos incluyendo especialidad", Toast.LENGTH_LONG).show()

                    } else {
                        postNewUser(firstName, lastName, username, email, password, speciality, true)
                    }

                } else {
                    if ((firstName == "") || (lastName == "") || (username == "") || (email == "") || (password == "")){
                        Toast.makeText(applicationContext, "Llene todos los campos", Toast.LENGTH_LONG).show()

                    } else {
                        postNewUser(firstName, lastName, username, email, password, speciality, false)

                    }
                }
            }

        }


    }
    fun postNewUser(firstName: String, lastName: String, userName:String, email:String, password:String, speciality:String, isCheecked: Boolean){
        val isDoctor = if (isCheecked){1}else{0}
        val doctorType = if (isCheecked){speciality}else{""}
        val response = Hostpital.service.newUser(userName, password, email, firstName, lastName, doctorType, isDoctor)
        response.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {

                if (response.body() != null && (response.code() < 300))
                {

                    val response = requireNotNull(response.body())

                    Toast.makeText(applicationContext, "Se ha creado con exito la cita", Toast.LENGTH_LONG).show()
                    Paper.book().destroy()
                    val intent = Intent(applicationContext, SplashActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)

                } else {
                    Toast.makeText(applicationContext, "Ya existe ese usuario", Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("fail token", "Error al general el token")

                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()

            }
        })

    }

}
