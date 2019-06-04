package com.partners.hostpital

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.partners.hostpital.api.API
import com.partners.hostpital.helpers.Constants
import com.partners.hostpital.models.CalendarDatesResponse
import com.partners.laboratorio7.Adapters.ViewHolder.CustomViewHolder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class MainFragment : Fragment() {

    lateinit var calendar: CalendarView
    var dates = emptyList<CalendarDatesResponse>()
    lateinit var recycler: RecyclerView
    lateinit var calendarInstance: Calendar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_main, container, false)
        calendarInstance = Calendar.getInstance()
        recycler = v.findViewById(R.id.dates_recycler_view)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = CalendarDatesAdapter()
        recycler.adapter?.notifyDataSetChanged()
        getAllDates(calendarInstance.get(Calendar.DAY_OF_MONTH), calendarInstance.get(Calendar.MONTH), calendarInstance.get(Calendar.YEAR))
        calendar = v.findViewById(R.id.calendar_dates)
        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            getAllDates(dayOfMonth, month, year)
        }
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
        val response = service.datesByDate(Paper.book().read(Constants.userId), Paper.book().read(Constants.isDoctor))

        response.enqueue(object : Callback<List<CalendarDatesResponse>> {
            override fun onResponse(call: Call<List<CalendarDatesResponse>>, response: Response<List<CalendarDatesResponse>>) {

                if (response.body() != null && (response.code() < 300)) {
                    val responseDates = requireNotNull(response.body())
                    dates = responseDates.filter {
                        it.date.year+1900 == year && it.date.date == dayOfMonth && it.date.month == month
                    }
                    recycler.adapter?.notifyDataSetChanged()

                } else {
                    Log.e("fail token", response.code().toString() + " - " + response.message())
                    Toast.makeText(this@MainFragment.activity, response.code().toString() + " - " + response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<CalendarDatesResponse>>, t: Throwable) {
                Log.e("fail token", t.message)
            }
        })
    }
    inner class CalendarDatesAdapter: RecyclerView.Adapter<CustomViewHolder>() {
        override fun getItemCount() = dates.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

            val date = dates[position]
            val view = holder.view
            val nameTxtV =view.findViewById<TextView>(R.id.name_textview)
            val reasonTxt =view.findViewById<TextView>(R.id.reason_text)
            val dateTxt =view.findViewById<TextView>(R.id.date_text)
            val button =view.findViewById<TextView>(R.id.date_calendar_btn)
            dateTxt.text = "Fecha: ${date.date}"
            reasonTxt.text =  "Razón: ${date.reason}"

            nameTxtV.text = if(Paper.book().read<Int>(Constants.isDoctor) == 1){
                "Nombre: ${date.patient.firstName} ${date.patient.lastName}"
            }else{
                "Nombre: ${date.doctor.firstName} ${date.doctor.lastName}"
            }
            button.setOnClickListener {
                val action = MainFragmentDirections.actionMainFragmentToSelectedDate(date)
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
