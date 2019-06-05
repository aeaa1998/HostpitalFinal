package com.partners.hostpital

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.partners.hostpital.api.API
import com.partners.hostpital.helpers.Constants
import com.partners.hostpital.models.CalendarDatesResponse
import com.partners.hostpital.requests.UpdateDateRequest
import io.paperdb.Paper
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.speech.SpeechRecognizer
import androidx.core.app.ActivityCompat


class DoReportFragmentDate : Fragment() {
    var selectedDate: CalendarDatesResponse? = null
    lateinit var recipe: EditText
    lateinit var observations: EditText
    lateinit var recordPermission: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedDate = DoReportFragmentDateArgs.fromBundle(requireArguments()).selectedDate

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_do_report_fragment_date, container, false)

        v.findViewById<ImageButton>(R.id.recipeVoice).isClickable = Paper.book().read(Constants.isDoctor, 0) == 1 && selectedDate?.status == 1

        recordPermission = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.RECORD_AUDIO)
        recipe = v.findViewById(R.id.recipe_textarea)
        observations = v.findViewById(R.id.observations_textarea)

        v.findViewById<ImageButton>(R.id.recipeVoice).setOnClickListener {
            if (!checkRecordPermission())
                permissionRecord()
            else {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                try {
                    startActivityForResult(intent, 10)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this.context, "No se puede utilizar esta funcion en su dispositivo", Toast.LENGTH_LONG).show()
                }
            }
        }
        v.findViewById<ImageButton>(R.id.observationsVoice).setOnClickListener {
            if (!checkRecordPermission())
                permissionRecord()
            else {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                try {
                    startActivityForResult(intent, 20)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this.context, "No se puede utilizar esta funcion en su dispositivo", Toast.LENGTH_LONG).show()
                }
            }
        }

        val b = v.findViewById<Button>(R.id.send_report)
        b.setOnClickListener {
            sendreport(v)
        }

        // Inflate the layout for this fragment
        return v
    }
    fun checkRecordPermission(): Boolean{
        val result =
                ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        return result
    }
    fun permissionRecord() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                            Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                        arrayOf(Manifest.permission.RECORD_AUDIO),1)
            }

        }
    }

    fun sendreport(v: View){
        recipe = v.findViewById(R.id.recipe_textarea)
        observations = v.findViewById(R.id.observations_textarea)
        if (recipe.text.toString() != "" && observations.text.toString() != ""){
            val service = API.request()
//            val response = service.processDate(requireNotNull(selectedDate?.id), 3, observations.text.toString(), recipe.text.toString())
            val body = UpdateDateRequest()
            body.observations =  observations.text.toString()
            body.status = 3
            body.recipe = recipe.text.toString()
            body.isDoctor =if(Paper.book().read<Int>(Constants.isDoctor) == 1){
                1
            }else{
                0
            }
            val response = service.processDate(requireNotNull(selectedDate?.id), body)
            response.enqueue(object : Callback<CalendarDatesResponse> {
                override fun onResponse(call: Call<CalendarDatesResponse>, response: Response<CalendarDatesResponse>) {

                    if (response.body() != null && (response.code() < 300)) {
                        val responseDates = requireNotNull(response.body())
//                        val action = DoReportFragmentDateDirections.actionDoReportFragmentDateToSelectedDate(response.body())
                        Toast.makeText(this@DoReportFragmentDate.context, "Se ha generado el reporte con exito", Toast.LENGTH_LONG).show()
                        NavHostFragment.findNavController(requireParentFragment()).popBackStack(R.id.mainFragment, false)


                    } else {
                        Log.e("fail token", response.code().toString() + " - " + response.message())
                        Toast.makeText(this@DoReportFragmentDate.activity, response.code().toString() + " - " + response.message(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<CalendarDatesResponse>, t: Throwable) {
                    Log.e("Error", t.message)
                }
            })
        }else{
            Toast.makeText(requireContext(), "Asegurese de llenar todos los datos.", Toast.LENGTH_LONG).show()
        }
    }


    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            10 -> {
                if (resultCode == Activity.RESULT_OK && data != null){
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    recipe.setText(result[0])
                }
            }
            20 -> {
                if (resultCode == Activity.RESULT_OK && data != null){
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    observations.setText(result[0])
                }
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
             if (grantResults.isNotEmpty()) {
                val audioAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (audioAccepted) {
                } else {
                    Toast.makeText(activity, "permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }


}
