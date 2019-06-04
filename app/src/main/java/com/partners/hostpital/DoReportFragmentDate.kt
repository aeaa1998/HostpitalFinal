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
import android.widget.Toast
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


class DoReportFragmentDate : Fragment() {
    var selectedDate: CalendarDatesResponse? = null
    lateinit var recipe: EditText
    lateinit var observations: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedDate = DoReportFragmentDateArgs.fromBundle(requireArguments()).selectedDate

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_do_report_fragment_date, container, false)
        val b = v.findViewById<Button>(R.id.send_report)
        b.setOnClickListener {
            sendreport(v)
        }

        // Inflate the layout for this fragment
        return v
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


}
