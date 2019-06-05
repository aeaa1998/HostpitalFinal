package com.partners.hostpital

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.partners.hostpital.helpers.Constants
import com.partners.hostpital.models.CalendarDatesResponse
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_do_report_fragment_date.*
import java.util.*
import java.util.jar.Manifest
import android.Manifest.permission
import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.speech.SpeechRecognizer
import android.util.Log


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

        v.findViewById<ImageButton>(R.id.recipeVoice).isClickable = Paper.book().read<Int>(Constants.isDoctor, 0) == 1 && selectedDate?.status == 1
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
