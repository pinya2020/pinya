package com.juan.pinya.view.main.monthlyStatistics

import android.app.DatePickerDialog
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.juan.pinya.R
import com.juan.pinya.di.SHARED_PREFERENCES_NAME
import com.juan.pinya.extention.setMountStarTime
import com.juan.pinya.model.DailyReport
import com.juan.pinya.model.Expenditure
import com.juan.pinya.model.Statistics
import com.juan.pinya.model.Stuff
import com.juan.pinya.module.SharedPreferencesManager
import com.juan.pinya.module.dailyReport.MonthlyStatisticsClickListener
import kotlinx.android.synthetic.main.activity_monthly_statistics.*
import kotlinx.android.synthetic.main.activity_monthly_statistics.month_textView
import kotlinx.android.synthetic.main.fragment_expenditure.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class MonthlyStatisticsActivity : AppCompatActivity(), MonthlyStatisticsClickListener {
    private val TAG = "MonthlyStatisticsActivity"
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val calendar = Calendar.getInstance().setMountStarTime()
    private val sharedPreferencesManager by inject<SharedPreferencesManager>(SHARED_PREFERENCES_NAME)
    private var allMeter = 0
    private var allExpenditure = 0
    private var statisticsList = mutableListOf<Statistics>()
    private val adapter = MonthlyStatisticsAdapter(statisticsList, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_statistics)
        title = getString(R.string.text_monthly_statistics)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val formatDate = SimpleDateFormat("yyyy/MM", Locale.getDefault())
        month_textView.text = formatDate.format(calendar.time)
        meter_rv.layoutManager = LinearLayoutManager(this)
        meter_rv.adapter = adapter
        getAllMeter(calendar)
        getAllExpenditure(calendar)
        statistics_month_imageButton.setOnClickListener {
            val editYear = calendar.get(Calendar.YEAR)
            val editMonth = calendar.get(Calendar.MONTH)
            val editDay = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(this,
                R.style.DatePickerDialog,
                { _, year, month, day ->
                    calendar.apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, day)
                    }
                    month_textView.text = formatDate.format(calendar.time)
                    allMeter=0
                    allExpenditure=0
                    statisticsList.clear()
                    getAllMeter(calendar)
                    getAllExpenditure(calendar)
                },
                editYear,
                editMonth,
                editDay)
            datePickerDialog.getDatePicker().findViewById<View>(Resources.getSystem()
                .getIdentifier("day", "id", "android")).setVisibility(View.GONE)
            datePickerDialog.show()
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    private fun getAllMeter(calendar: Calendar) {
        val todayMount = calendar.time
        calendar.add(Calendar.MONTH, 1)
        val tomorrowMount = calendar.time
        calendar.add(Calendar.MONTH, -1)

        val dailyReportSet = mutableSetOf<Statistics>()
        val dailyReportList = mutableListOf<Statistics>()
        db.collection(Stuff.DIR_NAME)
            .document(sharedPreferencesManager.stuffId)
            .collection(DailyReport.DIR_NAME)
            .whereGreaterThanOrEqualTo("date", Timestamp(todayMount))
            .whereLessThan("date", Timestamp(tomorrowMount))
            .get()
            .addOnSuccessListener { documents ->
                if (documents.count() == 0) {
                    Log.d(TAG, "沒有資料" + documents)
                } else {
                    val allDailyReportList = documents.toObjects(DailyReport::class.java)
                    for (i in allDailyReportList) {
                        allMeter += i.meter
                        val newDailyReport = Statistics(i.carId, i.company, 0)
                        val dailyReport = Statistics(i.carId, i.company, i.meter)
                        if (dailyReportSet.add(newDailyReport)) {
                            dailyReportList.add(dailyReport)
                        } else {
                            for (j in dailyReportList) {
                                if (j.carId.equals(i.carId) && j.company.equals(i.company)) {
                                    j.meter += i.meter
                                }
                            }
                        }
                    }
                }
                dailyReportList.sortWith(compareBy({ it.carId }, { it.company }))
                statisticsList.addAll(dailyReportList)
                adapter.updateList(statisticsList)
                adapter.notifyDataSetChanged()
                allMeter_textView.text = "${allMeter.toString()}米"
                Log.d(TAG, "expenditureList:" + statisticsList)
            }
    }

    private fun getAllExpenditure(calendar: Calendar) {
        val todayMount = calendar.time
        calendar.add(Calendar.MONTH, 1)
        val tomorrowMount = calendar.time
        calendar.add(Calendar.MONTH, -1)

        val expenditureSet = mutableSetOf<Statistics>()
        val expenditureList = mutableListOf<Statistics>()
        db.collection(Stuff.DIR_NAME)
            .document(sharedPreferencesManager.stuffId)
            .collection(Expenditure.DIR_NAME)
            .whereGreaterThanOrEqualTo("date", Timestamp(todayMount))
            .whereLessThan("date", Timestamp(tomorrowMount))
            .get()
            .addOnSuccessListener { documents ->
                if (documents.count() == 0) {
                    Log.d(TAG, "沒有資料" + documents)
                } else {
                    val allExpenditureList = documents.toObjects(Expenditure::class.java)
                    for (i in allExpenditureList) {
                        allExpenditure += i.price
                        val newExpenditure = Statistics(i.carId, i.type, 0, true)
                        val expenditure = Statistics(i.carId, i.type, i.price, true)
                        if (expenditureSet.add(newExpenditure)) {
                            expenditureList.add(expenditure)
                        } else {
                            for (j in expenditureList) {
                                if (j.carId.equals(i.carId) && j.company.equals(i.type)) {
                                    j.meter += i.price
                                }
                            }
                        }
                    }
                }
                allExpenditure_textView.text = "${allExpenditure.toString()}元"
                expenditureList.sortWith(compareBy({ it.carId }, { it.company }))
                for (i in 0 until expenditureList.size) {
                    statisticsList.add(i, expenditureList[i])
                }
                adapter.updateList(statisticsList)
                adapter.notifyDataSetChanged()
                Log.d(TAG, "expenditureList:" + statisticsList)

            }
    }


    override fun onRecyclerViewItemClick(view: View, statistics: Statistics) {
        val _statistics = statistics.copy(calender = calendar)
        if (statistics.expenditure){
            showFragment(MonthlyStatisticsFragment2.newInstance(_statistics))
        }else {
            showFragment(MonthlyStatisticsFragment.newInstance(_statistics))
        }
    }

    private fun showFragment(fragment: Fragment) {
        val manager: FragmentManager? = supportFragmentManager
        manager?.beginTransaction()?.apply {
            add(R.id.statistics_layout, fragment, fragment.tag)
            addToBackStack(null)
            commit()
        }
    }

}
