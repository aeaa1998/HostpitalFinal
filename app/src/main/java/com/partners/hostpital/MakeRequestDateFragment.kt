package com.partners.hostpital

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.partners.hostpital.api.Hostpital
import com.partners.hostpital.api.Hostpital.Companion.service
import com.partners.hostpital.helpers.Constants
import com.partners.hostpital.models.CalendarDatesResponse
import com.partners.hostpital.models.DoctorResponse
import com.partners.hostpital.models.PatientResponse
import com.partners.hostpital.models.TokenResponse
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Date
import java.time.LocalDate
import java.util.*


@Suppress("DEPRECATION")
class MakeRequestDateFragment : Fragment(), TimePickerDialog.OnTimeSetListener {


    lateinit var selectedPatient: PatientResponse
    lateinit var selectedDoctor: DoctorResponse
    var selectedDate = ""
    var selectedTime = ""
    lateinit var doctorNameView: TextView
    lateinit var doctorTypeView: TextView
    lateinit var reasonDate: EditText
    lateinit var patientNameView: TextView
    lateinit var uploadBtn: Button
    var day = 0
    lateinit var dateView: TextView
    lateinit var timeView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment =
        selectedDoctor = MakeRequestDateFragmentArgs.fromBundle(requireArguments()).selectedDoctor
        selectedPatient = MakeRequestDateFragmentArgs.fromBundle(requireArguments()).selectedPatient

        val v = inflater.inflate(R.layout.fragment_make_request_date, container, false)
        getAllViews(v)
        setAllViews(container)
        v.findViewById<Button>(R.id.select_date_btn).setOnClickListener {
            setDate(v)
        }

        v.findViewById<Button>(R.id.select_time_btn).setOnClickListener {
            setTime(v)
        }



        return v
    }
    private fun getAllViews(v : View){
        doctorNameView = v.findViewById(R.id.date_doctor_name_do)
        doctorTypeView = v.findViewById(R.id.date_doctor_type_do)
        patientNameView = v.findViewById(R.id.date_patient_name_do)
        dateView = v.findViewById(R.id.date_patient_date_text)
        timeView = v.findViewById(R.id.date_patient_time_text)
        reasonDate = v.findViewById(R.id.reason_view_date)
        uploadBtn = v.findViewById(R.id.upload_date_btn)
    }

    @SuppressLint("SetTextI18n")
    private fun setAllViews(container: ViewGroup?){
        val enterFormatted = Hostpital.newFormat.format(selectedDoctor.schedule.enterTime)
        val exitFormatted = Hostpital.newFormat.format(selectedDoctor.schedule.exitTime)
        doctorNameView.text = "${selectedDoctor.first_name} ${selectedDoctor.lastName}"
        doctorTypeView.text = "Especialidad: ${selectedDoctor.doctorType.name}"
        patientNameView.text = "${selectedPatient.firstName} ${selectedPatient.lastName}"
        uploadBtn.setOnClickListener {
            processDate()
        }
        val b = container?.rootView?.findViewById<View>(R.id.floating_btn_doctor)
        b?.visibility = View.GONE
        b?.isClickable = false
    }



    fun setDate(v: View){
        val now = Calendar.getInstance()

        val d = DatePickerDialog.newInstance{ view, year, monthOfYear, dayOfMonth ->
            val sday = if (dayOfMonth < 10){ "0$dayOfMonth" }else{ "$dayOfMonth" }
            val smonth = if (monthOfYear+1 < 10){ "0${monthOfYear+1}" }else{ "$monthOfYear" }
            day = dayOfMonth
            selectedDate = "$year-$smonth-$sday"
            dateView.text = selectedDate
        }
        d.minDate = now
        d.show(requireFragmentManager(), "Datepickerdialog")
    }
    fun setTime(v: View){
        val now = Calendar.getInstance()

        if (selectedDate!="") {
            if (now.get(Calendar.DAY_OF_MONTH) == day){
                Toast.makeText(requireContext(), "El dia de hoy no es valido escoja otra fecha", Toast.LENGTH_LONG).show()
            }else {


                val time = TimePickerDialog.newInstance(this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true)
                time.enableMinutes(false)
                time.is24HourMode
                time.enableSeconds(false)
                time.setLocale(Locale.getDefault())
                time.setMinTime(selectedDoctor.schedule.enterTime.hours, 0, 0)
                time.setMaxTime(selectedDoctor.schedule.exitTime.hours, 0, 0)
                time.onTimeSetListener = this
                time.show(requireFragmentManager(), "Datepickerdialog")
            }
        }else{
            Toast.makeText(requireContext(), "Escoja una fecha", Toast.LENGTH_LONG).show()

        }
    }
    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        val selectHour = if (hourOfDay < 10){ "0$hourOfDay" }else{ "$hourOfDay" }
        selectedTime = "$selectHour:00:00"
        timeView.text = selectedTime
    }


    private fun processDate(){
        val b = selectedDate != "" && selectedTime!= "" && reasonDate.text.toString() != ""
        if (b) {
            val isDoctor = Paper.book().read(Constants.patientId, 0)
            val response = service.postDate(selectedDoctor.id, selectedPatient.id, "$selectedDate $selectedTime", reasonDate.text.toString(), isDoctor)
            response.enqueue(object : Callback<CalendarDatesResponse?> {
                override fun onResponse(call: Call<CalendarDatesResponse?>, response: Response<CalendarDatesResponse?>) {

                    if (response.body() != null && (response.code() < 300))
                    {

                        val response = requireNotNull(response.body())

                        Toast.makeText(requireContext(), "Se ha creado con exito la cita", Toast.LENGTH_LONG).show()


                    } else {
                        Toast.makeText(requireContext(), "Ya existe esa cita", Toast.LENGTH_LONG).show()

                    }
                }

                override fun onFailure(call: Call<CalendarDatesResponse?>, t: Throwable) {
                    Log.e("fail token", "Error al general el token")

                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()

                }
            })
        }else{
            Toast.makeText(requireContext(), "Asegurese de llenar todos los campos", Toast.LENGTH_LONG).show()
        }
    }
}
