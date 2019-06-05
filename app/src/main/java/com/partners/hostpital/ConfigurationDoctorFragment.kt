package com.partners.hostpital

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.partners.hostpital.api.Hostpital.Companion.service
import com.partners.hostpital.helpers.Constants
import com.partners.hostpital.models.CalendarDatesResponse
import com.partners.hostpital.models.UserResponse
import com.partners.hostpital.requests.UpdateDateRequest
import com.partners.hostpital.requests.UpdateUserInfoRequest
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ConfigurationDoctorFragment : Fragment() {

    lateinit var newName: EditText
    lateinit var newLastName: EditText
    lateinit var newUsername: EditText
    lateinit var newPassword: EditText
    lateinit var newNameBtn: Button
    lateinit var newLastNameBtn: Button
    lateinit var newUsernameBtn: Button
    lateinit var newPasswordBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_configuration_doctor, container, false)
        // Inflate the layout for this fragment
        getAllViews(v)
        newNameBtn.setOnClickListener {
            updateInfo(newName, 1)
        }
        newLastNameBtn.setOnClickListener {
            updateInfo(newLastNameBtn, 2)
        }
        newUsernameBtn.setOnClickListener {
            updateInfo(newUsernameBtn, 3)
        }
        newPasswordBtn.setOnClickListener {
            updateInfo(newPasswordBtn, 4)
        }

        return v
    }


    private fun getAllViews(v : View){
        newLastName = v.findViewById(R.id.new_last_name_txt)
        newName = v.findViewById(R.id.new_name_txt)
        newUsername = v.findViewById(R.id.new_username_txt)
        newPassword = v.findViewById(R.id.new_password_txt)
        newLastNameBtn = v.findViewById(R.id.last_name_change_btn)
        newNameBtn = v.findViewById(R.id.name_change_btn)
        newUsernameBtn = v.findViewById(R.id.username_change_btn)
        newPasswordBtn = v.findViewById(R.id.password_change_btn)
    }

    fun updateInfo(tv: TextView, idx: Int){
        val body = UpdateUserInfoRequest()

        body.isDoctor =Paper.book().read(Constants.isDoctor, 0)
        if (tv.text.toString() != ""){
            when(idx){
                1 -> body.newName = tv.text.toString()
                2 -> body.newLastName = tv.text.toString()
                3 -> body.newUsername = tv.text.toString()
                4 -> body.newPassword = tv.text.toString()
            }
            val response = service.updateInfo(Paper.book().read(Constants.userId), body)
            response.enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {

                    if (response.body() != null && (response.code() < 300)) {
                        val response = requireNotNull(response.body())
//                        val action = DoReportFragmentDateDirections.actionDoReportFragmentDateToSelectedDate(response.body())
                        if (idx == 1 || idx == 2){
                            Paper.book().write(Constants.fullName, response.firstName + ' ' +response.lastName)
                        }
                        Toast.makeText(this@ConfigurationDoctorFragment.context, "Se ha cambiado la informacion con exito", Toast.LENGTH_LONG).show()

//                        NavHostFragment.findNavController(requireParentFragment()).popBackStack(R.id.mainFragment, false)


                    } else {
                        Log.e("fail token", response.code().toString() + " - " + response.message())
                        Toast.makeText(this@ConfigurationDoctorFragment.activity, response.code().toString() + " - " + response.message(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.e("Error", t.message)
                }
            })

        }else{
            Toast.makeText(context, "Llene el campo", Toast.LENGTH_LONG).show()
        }
    }


}
