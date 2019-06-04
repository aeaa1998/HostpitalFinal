package com.partners.hostpital

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.partners.hostpital.api.Hostpital
import com.partners.hostpital.helpers.Constants
import com.partners.hostpital.models.CalendarDatesResponse
import com.partners.hostpital.models.PatientResponse
import com.partners.laboratorio7.Adapters.ViewHolder.CustomViewHolder
import io.paperdb.Paper


class SelectedPatientFragment : Fragment() {
    lateinit var selectedPatient: PatientResponse
    lateinit var patientDoctorDates: List<CalendarDatesResponse>
    lateinit var patientImage: ImageView
    lateinit var patientEmail: TextView
    lateinit var patientName: TextView
    lateinit var patientDatesRecycler: RecyclerView
    lateinit var patientDateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        selectedPatient = SelectedPatientFragmentArgs.fromBundle(requireArguments()).selectedPatient
        val v = inflater.inflate(R.layout.fragment_selected_patient, container, false)
        patientDoctorDates = selectedPatient.dates.filter {
            it.doctorId ==  Paper.book().read(Constants.doctorId) && it.status > 1
        }
        getAllViews(v)
        setAllViews(container)
        setRecycler(v)
        patientDateButton.setOnClickListener {
            val action = SelectedPatientFragmentDirections.actionSelectedPatientFragmentToMakeRequestDateFragment(Paper.book().read(Constants.doctorUser), selectedPatient)
            NavHostFragment.findNavController(requireParentFragment()).navigate(action)
        }
        patientDatesRecycler.adapter?.notifyDataSetChanged()


        // Inflate the layout for this fragment
        return v
    }

    override fun onResume() {
        super.onResume()
        patientDatesRecycler.adapter?.notifyDataSetChanged()
    }


    private fun getAllViews(v : View){
        patientImage = v.findViewById(R.id.patient_image)
        patientEmail = v.findViewById(R.id.patient_email_single)
        patientName = v.findViewById(R.id.patient_name_single)
        patientDateButton = v.findViewById(R.id.patient_date_request_button)
    }


    @SuppressLint("SetTextI18n")
    private fun setAllViews(container: ViewGroup?){
        patientEmail.text = selectedPatient.email
        patientName.text = "${selectedPatient.firstName} ${selectedPatient.lastName}"
        val b = container?.rootView?.findViewById<View>(R.id.floating_btn_doctor)
        b?.visibility = View.GONE
        b?.isClickable = false
    }


    private fun setRecycler(v: View){
        patientDatesRecycler = v.findViewById(R.id.recycler_selected_patient_dates)
        patientDatesRecycler.layoutManager = LinearLayoutManager(context)
        patientDatesRecycler.adapter = SelectedPatientDates()
    }


    inner class SelectedPatientDates: RecyclerView.Adapter<CustomViewHolder>() {
        override fun getItemCount() = patientDoctorDates.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

            val date = patientDoctorDates[position]
            val view = holder.view
            val nameTxtV =view.findViewById<TextView>(R.id.name_textview)
            val reasonTxt =view.findViewById<TextView>(R.id.reason_text)
            val dateTxt =view.findViewById<TextView>(R.id.date_text)
            val button =view.findViewById<TextView>(R.id.date_calendar_btn)
            val dateFormatted = Hostpital.newFormatTime.format(date.date)
            dateTxt.text = "Fecha: $dateFormatted"
            reasonTxt.text =  "Razón: ${date.reason}"

            nameTxtV.text = if(Paper.book().read<Int>(Constants.isDoctor) == 1){
                "Nombre: ${date.patient.firstName} ${date.patient.lastName}"
            }else{
                "Nombre: ${date.doctor.firstName} ${date.doctor.lastName}"
            }
            button.setOnClickListener {
                val action = SelectedPatientFragmentDirections.actionSelectedPatientFragmentToSelectedDate(date)
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
