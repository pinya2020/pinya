package com.juan.pinya.module.Statistics

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.juan.pinya.R
import com.juan.pinya.di.SHARED_PREFERENCES_NAME
import com.juan.pinya.module.SharedPreferencesManager
import com.juan.pinya.view.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_statistics.*
import org.koin.android.ext.android.inject

class StatisticsFragment : Fragment() {
    private val sharedPreferencesManager by inject<SharedPreferencesManager>(SHARED_PREFERENCES_NAME)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logout_button.setOnClickListener {
            val intent = Intent()
            intent.setClass(requireContext(),LoginActivity::class.java)
            sharedPreferencesManager.isFirstLogin = true
            sharedPreferencesManager.carId = ""
            startActivity(intent)
        }
    }

}