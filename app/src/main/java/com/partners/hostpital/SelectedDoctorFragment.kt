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
import com.partners.hostpital.api.Hostpital
import com.partners.hostpital.helpers.Constants
import com.partners.hostpital.models.CalendarDatesResponse
import com.partners.hostpital.models.DoctorResponse
import io.paperdb.Paper


class SelectedDoctorFragment : Fragment() {

    lateinit var selectedDoctor: DoctorResponse
    lateinit var doctorImage: ImageView
    lateinit var doctorType: TextView
    lateinit var doctorName: TextView
    lateinit var doctorSchedule: TextView
    lateinit var doctorScheduleName: TextView
    lateinit var doctorDoDateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        selectedDoctor = SelectedDoctorFragmentArgs.fromBundle(requireArguments()).selectedDoctor
        val v = inflater.inflate(R.layout.fragment_selected_doctor, container, false)
        getAllViews(v)
        setAllViews(container)
        // Inflate the layout for this fragment
        return v
    }

    private fun getAllViews(v : View){
        doctorImage = v.findViewById(R.id.doctor_image)
        doctorType = v.findViewById(R.id.doctor_type_single)
        doctorName = v.findViewById(R.id.doctor_name_single)
        doctorSchedule = v.findViewById(R.id.doctor_schedule_single)
        doctorScheduleName = v.findViewById(R.id.doctor_schedule_name_single)
        doctorDoDateButton = v.findViewById(R.id.doctor_date_request_button)
    }
    @SuppressLint("SetTextI18n")
    private fun setAllViews(container: ViewGroup?){
        val enterFormatted = Hostpital.newFormat.format(selectedDoctor.schedule.enterTime)
        val exitFormatted = Hostpital.newFormat.format(selectedDoctor.schedule.exitTime)
        doctorDoDateButton.visibility = if (Paper.book().read<Int>(Constants.isDoctor) == 1){View.GONE}else{View.VISIBLE}
        doctorDoDateButton.isClickable = Paper.book().read<Int>(Constants.isDoctor) != 1
        doctorType.text = selectedDoctor.doctorType.name
        doctorName.text = "${selectedDoctor.first_name} ${selectedDoctor.lastName}"
        doctorSchedule.text = "Entrada: $enterFormatted - Salida: $exitFormatted"
        val b = container?.rootView?.findViewById<View>(R.id.floating_btn_doctor)
        b?.visibility = View.GONE
        b?.isClickable = false
    }


}

