package com.partners.hostpital

import android.annotation.SuppressLint
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
import com.partners.hostpital.helpers.Constants
import com.partners.hostpital.models.CalendarDatesResponse
import com.partners.laboratorio7.Adapters.ViewHolder.CustomViewHolder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


@Suppress("DEPRECATION")
class DatesRequestsFragment : Fragment() {
    var dates = emptyList<CalendarDatesResponse>()
    var filteredDates = emptyList<CalendarDatesResponse>()
    lateinit var filterByReason: EditText
    lateinit var calendarInstance: Calendar
    lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_dates_requests, container, false)
        filterByReason = v.findViewById(R.id.filter_dates_fragment_request)
        filterByReason.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filteredDates = dates.filter {
                    it.reason == s.toString() ||  it.reason.startsWith(s.toString())
                }

                recycler.adapter?.notifyDataSetChanged()
            }

        })
        calendarInstance = Calendar.getInstance()
        recycler = v.findViewById(R.id.recycler_row_dates_request)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = DatesRequestsAdapter()
        recycler.adapter?.notifyDataSetChanged()
        getAllDates(calendarInstance.get(Calendar.DAY_OF_MONTH), calendarInstance.get(Calendar.MONTH), calendarInstance.get(Calendar.YEAR))
        val b = container?.rootView?.findViewById<View>(R.id.floating_btn_doctor)
        b?.visibility = View.GONE
        b?.isClickable = false




        return v
    }
    override fun onResume() {
        super.onResume()
        getAllDates(calendarInstance.get(Calendar.DAY_OF_MONTH), calendarInstance.get(Calendar.MONTH), calendarInstance.get(Calendar.YEAR))
        recycler.adapter?.notifyDataSetChanged()

    }

    fun getAllDates(dayOfMonth: Int, month: Int, year: Int){
        val service = API.request()
        val response = service.pendingDates(Paper.book().read(Constants.userId))

        response.enqueue(object : Callback<List<CalendarDatesResponse>> {
            override fun onResponse(call: Call<List<CalendarDatesResponse>>, response: Response<List<CalendarDatesResponse>>) {

                if (response.body() != null && (response.code() < 300)) {
                    val responseDates = requireNotNull(response.body())
                    dates = responseDates.filter {
                        (it.date.year+1900 == year && it.date.date == dayOfMonth && it.date.month == month) || it.date.after(Date(year-1900, month, dayOfMonth))
                    }
                    recycler.adapter?.notifyDataSetChanged()

                } else {
                    Log.e("fail token", response.code().toString() + " - " + response.message())
                    Toast.makeText(this@DatesRequestsFragment.context, response.code().toString() + " - " + response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<CalendarDatesResponse>>, t: Throwable) {
                Log.e("fail token", t.message)
            }
        })
    }

    inner class DatesRequestsAdapter: RecyclerView.Adapter<CustomViewHolder>() {
        override fun getItemCount() = if (filterByReason.text.toString() == ""){dates.size}else{filteredDates.size}

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            val date = if (filterByReason.text.toString() == ""){dates[position]}else{filteredDates[position]}
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
            button.text = "Procesar"
            button.setOnClickListener {
                val action = DatesRequestsFragmentDirections.actionFragmentDatesRequestsToSelectedDate(date)
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

