package com.partners.hostpital

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.partners.hostpital.api.API
import com.partners.hostpital.api.Hostpital.Companion.newFormat
import com.partners.hostpital.helpers.Constants
import com.partners.hostpital.models.DoctorResponse
import com.partners.laboratorio7.Adapters.ViewHolder.CustomViewHolder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class DoctorsFragment : Fragment() {
    var doctors = emptyList<DoctorResponse>()
    lateinit var calendarInstance: Calendar
    lateinit var recycler: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_doctors, container, false)

        calendarInstance = Calendar.getInstance()
        setRecycler(v)
        recycler.adapter?.notifyDataSetChanged()
        getAllDoctors()
        val b = container?.rootView?.findViewById<View>(R.id.floating_btn_doctor)
        b?.visibility = View.GONE
        b?.isClickable = false


        return v
    }
    override fun onResume() {
        super.onResume()
        getAllDoctors()
        recycler.adapter?.notifyDataSetChanged()

    }

    private fun setRecycler(v: View){
        recycler = v.findViewById(R.id.recycler_row_doctors)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = DoctorsAdapter()
    }

    private fun getAllDoctors(){
        val service = API.request()
        val response = service.allDoctors(Paper.book().read(Constants.userId), Paper.book().read(Constants.isDoctor))

        response.enqueue(object : Callback<List<DoctorResponse>> {
            override fun onResponse(call: Call<List<DoctorResponse>>, response: Response<List<DoctorResponse>>) {

                if (response.body() != null && (response.code() < 300)) {
                    val responseDoctors = requireNotNull(response.body())
                    doctors = responseDoctors
                    recycler.adapter?.notifyDataSetChanged()

                } else {
                    Log.e("fail token", response.code().toString() + " - " + response.message())
                    Toast.makeText(this@DoctorsFragment.context, response.code().toString() + " - " + response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<DoctorResponse>>, t: Throwable) {
                Log.e("fail token", t.message)
            }
        })
    }

    inner class DoctorsAdapter: RecyclerView.Adapter<CustomViewHolder>() {
        override fun getItemCount() = doctors.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

            val doctor = doctors[position]
            val view = holder.view
            val nameTxtV =view.findViewById<TextView>(R.id.name_textview)
            val schedule =view.findViewById<TextView>(R.id.reason_text)
            val doctorType =view.findViewById<TextView>(R.id.date_text)
            val button =view.findViewById<TextView>(R.id.date_calendar_btn)

            nameTxtV.text = "Doctor: ${doctor.first_name} ${doctor.lastName}"
            doctorType.text = doctor.doctorType.name
            val enterFormatted = newFormat.format(doctor.schedule.enterTime)
            val exitFormatted = newFormat.format(doctor.schedule.exitTime)
            schedule.text = "Ingreso: ${enterFormatted} - Salida: ${exitFormatted}"
            button.setOnClickListener {
                val action = DoctorsFragmentDirections.actionDoctorsFragmentToSelectedDoctorFragment(doctor)
                NavHostFragment.findNavController(requireParentFragment()).navigate(action)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CustomViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val cellForRow = layoutInflater.inflate(R.layout.muti_uses_row, parent, false)

            return CustomViewHolder(cellForRow)

        }

    }


}
