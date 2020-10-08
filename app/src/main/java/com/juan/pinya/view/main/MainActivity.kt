package com.juan.pinya.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.juan.pinya.R
import com.juan.pinya.module.Expenditure.ExpenditureFragment
import com.juan.pinya.module.Statistics.StatisticsFragment
import com.juan.pinya.view.main.dailyReport.DailyReportFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val dailyReportFragment by lazy { DailyReportFragment() }
    private val expenditureFragment by lazy { ExpenditureFragment() }
    private val statisticsFragment by lazy { StatisticsFragment() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //NavigationButton
        btnav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_dailyreport -> makeCurrentFragment(dailyReportFragment)
                R.id.ic_expenditure -> makeCurrentFragment(expenditureFragment)
                R.id.ic_statistics -> makeCurrentFragment(statisticsFragment)
            }
            true
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment, dailyReportFragment).commit()
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment)
            commit()
        }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}