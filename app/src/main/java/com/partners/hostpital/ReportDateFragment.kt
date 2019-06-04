package com.partners.hostpital

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.partners.hostpital.helpers.Constants
import com.partners.hostpital.models.CalendarDatesResponse
import io.paperdb.Paper


class ReportDateFragment : Fragment() {

    var selectedDate: CalendarDatesResponse? = null
    lateinit var recipe: EditText
    lateinit var role: TextView
    lateinit var observations: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedDate = ReportDateFragmentArgs.fromBundle(requireArguments()).selectedDate

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val v = inflater.inflate(R.layout.fragment_report_date, container, false)
        recipe = v.findViewById(R.id.recipe_container)
        observations = v.findViewById(R.id.observations_container)
        role = v.findViewById(R.id.role_txt_selected_holder)
        recipe.setText(selectedDate?.report?.recipe)
        observations.setText(selectedDate?.report?.observations)
        recipe.isEnabled = false
        observations.isEnabled = false
        role.text = if (Paper.book().read<Int>(Constants.isDoctor) == 1){
            "Paciente"
        }else{
            "Doctor"
        }
        v.findViewById<TextView>(R.id.role_txt_selected).text = if (Paper.book().read<Int>(Constants.isDoctor) == 1){
            "${selectedDate?.patient?.firstName} ${selectedDate?.patient?.lastName}"
        }else{
            "${selectedDate?.doctor?.firstName} ${selectedDate?.doctor?.lastName}"
        }
        return v
    }

}
