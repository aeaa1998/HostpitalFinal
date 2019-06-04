package com.partners.hostpital

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.partners.hostpital.models.DoctorResponse
import com.partners.hostpital.models.PatientResponse


class MakeRequestDateFragment : Fragment() {
    lateinit var selectedPatient: PatientResponse
    lateinit var selectedDoctor: DoctorResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        selectedPatient = MakeRequestDateFragmentArgs.fromBundle(requireArguments()).selectedPatient
        selectedDoctor = MakeRequestDateFragmentArgs.fromBundle(requireArguments()).selectedDoctor

        return inflater.inflate(R.layout.fragment_make_request_date, container, false)
    }

}
