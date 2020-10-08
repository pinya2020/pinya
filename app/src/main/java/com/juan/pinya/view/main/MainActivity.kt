package com.juan.pinya.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.juan.pinya.R
import com.juan.pinya.fragments.ExpenditureFragment
import com.juan.pinya.fragments.StatisticsFragment
import com.juan.pinya.fragments.dailyreport.Communicator
import com.juan.pinya.fragments.dailyreport.DRAdd2Fragment
import com.juan.pinya.fragments.dailyreport.DailyReportFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Communicator {

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

    override fun passDataCom(msg: String) {
        val bundle = Bundle()
        bundle.putString("input_text", msg)

        val transaction = this.supportFragmentManager.beginTransaction()
        val frag2 = DRAdd2Fragment()
        frag2.arguments = bundle

        transaction.replace(R.id.dailyReport_ConstraintLayout, frag2)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}