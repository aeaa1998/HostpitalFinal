package com.partners.hostpital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.partners.hostpital.api.API
import com.partners.hostpital.helpers.Constants
import com.partners.hostpital.models.TokenResponse
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
    fun login(v: View){
        val service = API.request()
        val response = service.oauthToken("password", 4, "25fIpbW5RNhc6nUHjHEnttfk7LcKf7oU39NfJ0bM", user_edit_txt.text.toString(), password_edit_txt.text.toString(), "*")


        response.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {

                if (response.body() != null && (response.code() < 300))
                {
                    val responseToken = requireNotNull(response.body())
                    Log.d("token", response.body()?.accessToken)
                    Toast.makeText(this@LoginActivity, "Se ha logeado correctamente", Toast.LENGTH_LONG).show()
                    Paper.book().write(Constants.accessToken, responseToken.accessToken)
                    Paper.book().write(Constants.isDoctor, responseToken.isDoctor)
                    Paper.book().write(Constants.userId, responseToken.id)
                    Paper.book().write(Constants.fullName, responseToken.firstName + ' ' +responseToken.lastName)
                    Paper.book().write(Constants.patientId, responseToken.patientId)

                    if (responseToken.isDoctor == 1){
                        Paper.book().write(Constants.doctorId, responseToken.doctorId)

                        val i = Intent(this@LoginActivity, ChooseApp::class.java)
                        startActivity(i)
                    }else{
                        val i = Intent(this@LoginActivity, PatientActivity::class.java)
                        startActivity(i)
                    }
                } else {
                    Log.e("fail token", response.code().toString() + " - " + response.message())
                    Toast.makeText(this@LoginActivity, response.code().toString() + " - " + response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Log.e("fail token", "Error al general el token")
            }
        })
    }
}
