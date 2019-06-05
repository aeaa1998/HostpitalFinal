package com.partners.hostpital

import android.annotation.SuppressLint
import android.content.Context
import android.inputmethodservice.Keyboard
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.partners.hostpital.api.API
import com.partners.hostpital.api.Hostpital
import com.partners.hostpital.api.Hostpital.Companion.hideKeyboardFrom
import com.partners.hostpital.helpers.Constants
import com.partners.hostpital.models.CalendarDatesResponse
import com.partners.hostpital.models.DoctorResponse
import com.partners.hostpital.models.PatientResponse
import com.partners.laboratorio7.Adapters.ViewHolder.CustomViewHolder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class PatientsFragment : Fragment() {
    var patients = emptyList<PatientResponse>()
    var filteredPatients = emptyList<PatientResponse>()
    lateinit var calendarInstance: Calendar
    lateinit var recycler: RecyclerView
    lateinit var filterByName: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_patients, container, false)
        filterByName = v.findViewById(R.id.filter_patients_fragment)
        calendarInstance = Calendar.getInstance()
        setRecycler(v)
        recycler.adapter?.notifyDataSetChanged()
        getAllPatients()
        val b = container?.rootView?.findViewById<View>(R.id.floating_btn_doctor)
        b?.visibility = View.GONE
        b?.isClickable = false

        filterByName.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filteredPatients = patients.filter {
                    it.firstName == s.toString() || it.lastName == s.toString() || it.firstName.startsWith(s.toString()) ||  it.lastName.startsWith(s.toString())
                }

                recycler.adapter?.notifyDataSetChanged()
            }

        })

        return v
    }
    private fun getAllPatients(){
        val service = API.request()
        val response = service.allPatientsDoctor(Paper.book().read(Constants.userId))

        response.enqueue(object : Callback<List<PatientResponse>> {
            override fun onResponse(call: Call<List<PatientResponse>>, response: Response<List<PatientResponse>>) {

                if (response.body() != null && (response.code() < 300)) {
                    val responseDoctors = requireNotNull(response.body())
                    patients = responseDoctors
                    recycler.adapter?.notifyDataSetChanged()

                } else {
                    Log.e("fail token", response.code().toString() + " - " + response.message())
                    Toast.makeText(this@PatientsFragment.context, response.code().toString() + " - " + response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<PatientResponse>>, t: Throwable) {
                Log.e("fail token", t.message)
            }
        })
    }

    private fun setRecycler(v: View){
        recycler = v.findViewById(R.id.recycler_row_patients)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = PatientsAdapter()
    }

    inner class PatientsAdapter: RecyclerView.Adapter<CustomViewHolder>() {
        override fun getItemCount() = if (filterByName.text.toString() == ""){patients.size}else{filteredPatients.size}

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

            val patient = if (filterByName.text.toString() == ""){patients[position]}else{filteredPatients[position]}
            val view = holder.view
            val patientText =view.findViewById<TextView>(R.id.name_textview)
            val patientName =view.findViewById<TextView>(R.id.date_text)
            val email =view.findViewById<TextView>(R.id.reason_text)
            val button =view.findViewById<TextView>(R.id.date_calendar_btn)
            patientText.text = "Paciente"
            patientName.text = "${patient.firstName} ${patient.lastName}"
            email.text = "e-mail: ${patient.email}"

            button.setOnClickListener {
                hideKeyboardFrom(requireContext(), view)
                val action = PatientsFragmentDirections.actionPatientsFragmentToSelectedPatientFragment(patient)
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
