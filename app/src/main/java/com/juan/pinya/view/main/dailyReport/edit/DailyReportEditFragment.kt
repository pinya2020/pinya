package com.juan.pinya.view.main.dailyReport.edit

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.IntegerRes
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.juan.pinya.R
import com.juan.pinya.di.SHARED_PREFERENCES_NAME
import com.juan.pinya.model.DailyReport
import com.juan.pinya.model.Stuff
import com.juan.pinya.module.SharedPreferencesManager
import com.juan.pinya.module.dailyReport.BaseFragment
import com.juan.pinya.view.main.dailyReport.address.AddressFragment
import com.juan.pinya.view.main.dailyReport.company.CompanyFragment
import com.juan.pinya.view.main.dailyReport.site.SiteFragment
import kotlinx.android.synthetic.main.fragment_daily_report_add4.*
import kotlinx.android.synthetic.main.fragment_daily_report_add4.data_imageButton
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class DailyReportEditFragment : BaseFragment() {
    private val TAG = "DRAdd4Fragment"
    private lateinit var newDailyReport: DailyReport
    private val cal = Calendar.getInstance()
    private var nowYear = cal.get(Calendar.YEAR)
    private var nowMonth = cal.get(Calendar.MONTH)
    private var nowDay = cal.get(Calendar.DAY_OF_MONTH)
    private var nowHour = cal.get(Calendar.HOUR_OF_DAY)
    private var nowMinute = cal.get(Calendar.MINUTE)
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val sharedPreferencesManager by inject<SharedPreferencesManager>(SHARED_PREFERENCES_NAME)
    private val calendar = Calendar.Builder()
        .set(Calendar.YEAR, nowYear)
        .set(Calendar.MONTH, nowMonth)
        .set(Calendar.DAY_OF_MONTH, nowDay)
        .set(Calendar.HOUR, nowHour)
        .set(Calendar.MINUTE, nowMinute)
        .build()
    private var date = Timestamp(calendar.time)
    private var carId = ""
    override var bottomNavigationViewVisibility = View.GONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.apply {
            newDailyReport = getParcelable(DAILY_REPORT_KEY) ?: return@apply
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_report_add4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (newDailyReport.id != null) {
            add4_textView.text = getString(R.string.text_updata_dailyreport)
        } else {
            add4_textView.text = getString(R.string.text_add_dailyreport)
        }
        carId = newDailyReport.carId
        add4_carId_textView.text = carId
        add4_carId_imageButton.setOnClickListener {
            showCarIdDialog()
        }
        add4_company_textView.text = newDailyReport.company
        add4_site_textView.text = newDailyReport.site
        add4_address_textView.text = newDailyReport.address
        add4_price_textView.text = newDailyReport.price.toString()

        val formatDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        add4_date_textView.text = formatDate.format(newDailyReport.date.toDate())
        val formatTime = SimpleDateFormat("HH:mm", Locale.getDefault())
        add4_time_textView.text = formatTime.format(newDailyReport.date.toDate())

        if (newDailyReport.meter != Int.MIN_VALUE) {
            add4_meter_editText.setText(newDailyReport.meter.toString())
        }
        if (newDailyReport.number != Int.MIN_VALUE) {
            add4_number_editText.setText(newDailyReport.number.toString())
        }
        //EditTextView actionDone + Multiline
        add4_ps_editText.setHorizontallyScrolling(false)
        add4_ps_editText.maxLines = Int.MAX_VALUE
        add4_ps_editText.setText(newDailyReport.ps)

        add4_company_textView.setOnClickListener {
            val fragment = CompanyFragment.newInstance(newDailyReport.id)
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.dailyReport_constraintLayout, fragment, fragment.tag)
                addToBackStack(null)
                commit()
            }
        }
        add4_site_textView.setOnClickListener {
            val fragment = SiteFragment.newInstance(newDailyReport)
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.dailyReport_constraintLayout, fragment, fragment.tag)
                addToBackStack(null)
                commit()
            }
        }
        add4_address_textView.setOnClickListener {
            val fragment = AddressFragment.newInstance(newDailyReport)
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.dailyReport_constraintLayout, fragment, fragment.tag)
                addToBackStack(null)
                commit()
            }
        }

        if (newDailyReport.date != Timestamp(Date())) {
            date = newDailyReport.date
        }

        data_imageButton.setOnClickListener {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            this.context?.let { it1 ->
                DatePickerDialog(it1, { _, year, month, day ->
                    nowYear = year
                    nowMonth = month
                    nowDay = day
                    val showDate = "$nowYear/${nowMonth + 1}/$nowDay"
                    val calendar = Calendar.Builder()
                        .set(Calendar.YEAR, nowYear)
                        .set(Calendar.MONTH, nowMonth)
                        .set(Calendar.DAY_OF_MONTH, nowDay)
                        .set(Calendar.HOUR, nowHour)
                        .set(Calendar.MINUTE, nowMinute)
                        .build()
                    date = Timestamp(calendar.time)
                    add4_date_textView.text = showDate
                }, year, month, day).show()
            }
        }

        time_imageButton.setOnClickListener {
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)
            TimePickerDialog(requireContext(), { _, setHour, setMinute ->
                nowHour = setHour
                nowMinute = setMinute
                val calendar = Calendar.Builder()
                    .set(Calendar.YEAR, nowYear)
                    .set(Calendar.MONTH, nowMonth)
                    .set(Calendar.DAY_OF_MONTH, nowDay)
                    .set(Calendar.HOUR, nowHour)
                    .set(Calendar.MINUTE, nowMinute)
                    .build()
                date = Timestamp(calendar.time)
                val showTime = "$nowHour:$nowMinute"
                add4_time_textView.text = showTime
            }, hour, minute, true).show()
        }

        add4_finish_button.setOnClickListener {

            if (add4_meter_editText.text.isEmpty()) {
                add4_meter_editText.error = "請輸入米數"
                if (add4_number_editText.text.isEmpty()) {
                    add4_number_editText.error = "請輸入車次"
                }
                return@setOnClickListener
            }
            val meter = add4_meter_editText.text.toString().toInt()
            if (add4_number_editText.text.isEmpty()) {
                add4_number_editText.error = "請輸入車次"
                return@setOnClickListener
            }
            val number = add4_number_editText.text.toString().toInt()
            val ps = add4_ps_editText.text.toString()
            val dailyReport = newDailyReport.copy(
                carId = carId,
                date = date,
                meter = meter,
                number = number,
                ps = ps
            )
            if (newDailyReport.id == null) {
                addDailyReport(dailyReport)
            } else {
                setDailyReport(dailyReport)
            }
        }

        add4_cancel_button.setOnClickListener {
            parentFragmentManager.popBackStack(null, 1)
        }
    }

    private fun addDailyReport(dailyReport: DailyReport) {
        val ref = db.collection(Stuff.DIR_NAME).document(sharedPreferencesManager.stuffId)
            .collection(DailyReport.DIR_NAME).document()
        val addDailyReport = dailyReport.copy(id = ref.id)
        ref.set(addDailyReport)
            .addOnSuccessListener {
                setAdminDailyReport(addDailyReport)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error addDailyReport", exception)
            }
    }

    private fun setDailyReport(dailyReport: DailyReport) {
        db.collection(Stuff.DIR_NAME).document(sharedPreferencesManager.stuffId)
            .collection(DailyReport.DIR_NAME)
            .document(dailyReport.id ?: return)
            .set(dailyReport)
            .addOnSuccessListener {
                setAdminDailyReport(dailyReport)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error setDailyReport", exception)
            }
    }

    private fun setAdminDailyReport(dailyReport: DailyReport) {
        db.collection(DailyReport.DIR_NAME)
            .document(dailyReport.id ?: return)
            .set(dailyReport)
            .addOnSuccessListener {
                parentFragmentManager.popBackStack(null, 1)
            }
            .addOnFailureListener { Log.w(TAG, "Error addDailyReport", it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.setGroupVisible(0, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val newCarId = data?.getStringExtra("carId")
            carId = newCarId.toString()
            add4_carId_textView.text = carId
        }
    }

    companion object {
        private const val DAILY_REPORT_KEY = "dailyReport"
        fun newInstance(dailyReport: DailyReport): DailyReportEditFragment {
            return DailyReportEditFragment().apply {
                arguments = Bundle().also { bundle ->
                    bundle.putParcelable(DAILY_REPORT_KEY, dailyReport)
                }
            }
        }
    }
}
