package com.juan.pinya.view.main.dailyReport.edit

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
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
    private lateinit var dailyReport: DailyReport
    private val cal = Calendar.getInstance()
    private var nowYear = cal.get(Calendar.YEAR)
    private var nowMonth = cal.get(Calendar.MONTH)
    private var nowDay = cal.get(Calendar.DAY_OF_MONTH)
    private var nowHour = cal.get(Calendar.HOUR_OF_DAY)
    private var nowMinute = cal.get(Calendar.MINUTE)
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val sharedPreferencesManager by inject<SharedPreferencesManager>(SHARED_PREFERENCES_NAME)
//    private val calendar = Calendar.Builder()
//        .set(Calendar.YEAR, nowYear)
//        .set(Calendar.MONTH, nowMonth)
//        .set(Calendar.DAY_OF_MONTH, nowDay)
//        .set(Calendar.HOUR, nowHour)
//        .set(Calendar.MINUTE, nowMinute)
//        .build()
    private var date = Timestamp(cal.time)
    private val formatHour = SimpleDateFormat("HH", Locale.getDefault())
    private val formatMinute = SimpleDateFormat("mm", Locale.getDefault())
    override var bottomNavigationViewVisibility = View.GONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.apply {
            dailyReport = getParcelable(DAILY_REPORT_KEY) ?: return@apply
        }

        cal.set(nowYear, nowMonth, nowDay, nowMinute, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_report_add4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (dailyReport.id != null) {
            add4_textView.text = getString(R.string.text_updata_dailyreport)
            add4_ps_editText.setText(dailyReport.ps)
            hour_editTextNumber.setText(formatHour.format(dailyReport.date.toDate()))
            minute_editTextNumber.setText(formatMinute.format(dailyReport.date.toDate()))

        }

        if(dailyReport.meter != Int.MIN_VALUE){
            add4_meter_editText.setText(dailyReport.meter.toString())
        }
        if (dailyReport.number != Int.MIN_VALUE){
            add4_number_editText.setText(dailyReport.number.toString())
        }

        add4_meter_editText.addTextChangedListener(textWatcher)
        add4_number_editText.addTextChangedListener(textWatcher)
//        hour_editTextNumber.addTextChangedListener(textWatcher)
//        minute_editTextNumber.addTextChangedListener(textWatcher)
        //EditTextView actionDone + Multiline
        add4_ps_editText.setHorizontallyScrolling(false)
        add4_ps_editText.maxLines = Int.MAX_VALUE

        add4_carId_imageButton.setOnClickListener {
            add4_carId_textView.error = null
            showCarIdDialog(false)
        }

        add4_carId_textView.text = dailyReport.carId
        add4_company_textView.text = dailyReport.company
        add4_site_textView.text = dailyReport.site
        add4_address_textView.text = dailyReport.address
        add4_price_textView.text = dailyReport.price.toString()

        if (dailyReport.date != Timestamp(Date())) {
            date = dailyReport.date
            val formatYear = SimpleDateFormat("yyyy", Locale.getDefault())
            val formatMount = SimpleDateFormat("MM", Locale.getDefault())
            val formatDay = SimpleDateFormat("dd", Locale.getDefault())
            nowYear = formatYear.format(date.toDate()).toInt()
            nowMonth = formatMount.format(date.toDate()).toInt()-1
            nowDay = formatDay.format(date.toDate()).toInt()
        }

        val formatDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        add4_date_textView.text = formatDate.format(dailyReport.date.toDate())

        hour_editTextNumber.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                hour_editTextNumber.setText("")
            }
        }

        minute_editTextNumber.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                minute_editTextNumber.setText("")
            }
        }

        time_imageButton.setOnClickListener {
            hour_editTextNumber.error = null
            minute_editTextNumber.error = null
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)
            TimePickerDialog(requireContext(), { _, setHour, setMinute ->
                hour_editTextNumber.setText(setHour.toString())
                minute_editTextNumber.setText(setMinute.toString())
            }, hour, minute, true).show()
        }

        add4_company_textView.setOnClickListener {
            val fragment = CompanyFragment.newInstance(dailyReport.id)
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.dailyReport_constraintLayout, fragment, fragment.tag)
                addToBackStack(null)
                commit()
            }
        }

        add4_site_textView.setOnClickListener {
            val fragment = SiteFragment.newInstance(dailyReport)
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.dailyReport_constraintLayout, fragment, fragment.tag)
                addToBackStack(null)
                commit()
            }
        }

        add4_address_textView.setOnClickListener {
            val fragment = AddressFragment.newInstance(dailyReport)
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.dailyReport_constraintLayout, fragment, fragment.tag)
                addToBackStack(null)
                commit()
            }
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
                    add4_date_textView.text = showDate
                }, year, month, day).show()
            }
        }

        add4_finish_button.setOnClickListener {
            if (add4_carId_textView.text.isEmpty()) {
                add4_carId_textView.error = getString(R.string.text_please_select_car_id)
                if (add4_meter_editText.text.isEmpty() || add4_meter_editText.text.toString()
                        .toInt() == 0
                ) {
                    add4_meter_editText.error = getString(R.string.text_please_import_meter)
                    if (add4_number_editText.text.isEmpty() || add4_number_editText.text.toString()
                            .toInt() == 0
                    ) {
                        add4_number_editText.error =
                            getString(R.string.text_please_import_car_number)
                    }
                }
                return@setOnClickListener
            }

            if (add4_meter_editText.text.isEmpty() || add4_meter_editText.text.toString()
                    .toInt() == 0
            ) {
                add4_meter_editText.error = getString(R.string.text_please_import_meter)
                if (add4_number_editText.text.isEmpty() || add4_number_editText.text.toString()
                        .toInt() == 0
                ) {
                    add4_number_editText.error = getString(R.string.text_please_import_car_number)
                }
                return@setOnClickListener
            }

            if (add4_number_editText.text.isEmpty() || add4_number_editText.text.toString()
                    .toInt() == 0
            ) {
                add4_number_editText.error = getString(R.string.text_please_import_car_number)
                return@setOnClickListener
            }

            if (hour_editTextNumber.text.isEmpty()){
                hour_editTextNumber.error = getString(R.string.text_please_import_correct_hour)
                if (minute_editTextNumber.text.toString().isEmpty()){
                    minute_editTextNumber.error = getString(R.string.text_please_import_correct_minute)
                }
                return@setOnClickListener
            }

            if (minute_editTextNumber.text.toString().isEmpty()){
                minute_editTextNumber.error = getString(R.string.text_please_import_correct_minute)
                return@setOnClickListener
            }

            nowHour = hour_editTextNumber.text.toString().toInt()
            nowMinute = minute_editTextNumber.text.toString().toInt()
            if (nowHour < 0 || nowHour > 23) {
                hour_editTextNumber.error = getString(R.string.text_please_import_correct_hour)
                if (nowMinute < 0 || nowMinute > 60) {
                    minute_editTextNumber.error = getString(R.string.text_please_import_correct_minute)
                }
                return@setOnClickListener
            }

            if (nowMinute < 0 || nowMinute > 60) {
                minute_editTextNumber.error = getString(R.string.text_please_import_correct_minute)
                return@setOnClickListener
            }

            if(add4_site_textView.text.equals(getString(R.string.text_other)) && add4_ps_editText.text.isEmpty()){
                add4_ps_editText.error = getString(R.string.text_please_import_ps)
                return@setOnClickListener
            }

//            val calendar = Calendar.Builder()
//                .set(Calendar.YEAR, nowYear)
//                .set(Calendar.MONTH, nowMonth)
//                .set(Calendar.DAY_OF_MONTH, nowDay)
//                .set(Calendar.HOUR, nowHour)
//                .set(Calendar.MINUTE, nowMinute)
//                .build()

            cal.set(nowYear,nowMonth,nowDay,nowHour,nowMinute,0)
            date = Timestamp(cal.time)
            val carId = add4_carId_textView.text.toString()
            val meter = add4_meter_editText.text.toString().toInt()
            val number = add4_number_editText.text.toString().toInt()
            val ps = add4_ps_editText.text.toString()
            val newDailyReport = dailyReport.copy(
                carId = carId,
                date = date,
                meter = meter,
                number = number,
                ps = ps
            )

            add4_finish_button.isEnabled = false
            if (isOnline(requireContext())) {
                if (dailyReport.id == null) {
                    val ref = db
                        .collection(Stuff.DIR_NAME)
                        .document(newDailyReport.userId)
                        .collection(DailyReport.DIR_NAME).document()
                    val addDailyReport = newDailyReport.copy(id = ref.id)
                    setDailyReport(addDailyReport)
                    setAdminDailyReport(addDailyReport)
                } else {
                    setDailyReport(newDailyReport)
                    setAdminDailyReport(newDailyReport)
                }
            } else {
                add4_finish_button.isEnabled = true
                Toast.makeText(requireContext(),
                    getString(R.string.text_please_check_internet),
                    Toast.LENGTH_SHORT).show()
            }

        }

        add4_cancel_button.setOnClickListener {
            parentFragmentManager.popBackStack(null, 1)
        }
    }

    private fun setDailyReport(dailyReport: DailyReport) {
        db.collection(Stuff.DIR_NAME)
            .document(dailyReport.userId)
            .collection(DailyReport.DIR_NAME)
            .document(dailyReport.id ?: return)
            .set(dailyReport)
            .addOnSuccessListener {
                Log.d(TAG, "DailyReport 新增成功")
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error setDailyReport", exception)
            }
    }

    private fun setAdminDailyReport(dailyReport: DailyReport) {
        db.collection(DailyReport.DIR_NAME)
            .document(dailyReport.id ?: return)
            .set(dailyReport)
            .addOnSuccessListener {
                Log.d(TAG, "AdminDailyReport 新增成功")
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
            add4_carId_textView.text = newCarId
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
        override fun afterTextChanged(s: Editable?) {
            if (s.toString().length > 1 && s.toString().startsWith("0")) {
                s?.replace(0, 1, "")
            }
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
