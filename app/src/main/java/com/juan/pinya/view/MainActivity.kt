package com.juan.pinya.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.juan.pinya.R
import com.juan.pinya.fragments.DailyReportFragment
import com.juan.pinya.fragments.ExpenditureFragment
import com.juan.pinya.fragments.StatisticsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dailyReportFragment = DailyReportFragment()
        val expenditureFragment = ExpenditureFragment()
        val statisticsFragment = StatisticsFragment()

        btnav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_dailyreport -> makeCurrentFragment(dailyReportFragment)
                R.id.ic_expenditure -> makeCurrentFragment(expenditureFragment)
                R.id.ic_statistics -> makeCurrentFragment(statisticsFragment)
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment)
            commit()
        }

    companion object {
        private const val ID_KEY = "id"
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}