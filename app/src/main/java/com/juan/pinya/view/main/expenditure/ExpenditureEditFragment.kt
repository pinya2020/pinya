package com.juan.pinya.view.main.expenditure

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.juan.pinya.R
import com.juan.pinya.di.SHARED_PREFERENCES_NAME
import com.juan.pinya.model.Expenditure
import com.juan.pinya.model.Stuff
import com.juan.pinya.module.SharedPreferencesManager
import com.juan.pinya.module.dailyReport.BaseFragment
import kotlinx.android.synthetic.main.fragment_daily_report_add4.*
import kotlinx.android.synthetic.main.fragment_expenditure_edit.*
import kotlinx.android.synthetic.main.fragment_expenditure_edit.add4_textView
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class ExpenditureEditFragment : BaseFragment() {
    private val TAG = "ExpenditureEditFragment"
    override var bottomNavigationViewVisibility = View.GONE
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val sharedPreferencesManager by inject<SharedPreferencesManager>(SHARED_PREFERENCES_NAME)
    private lateinit var newExpenditure: Expenditure
    private val cal = Calendar.getInstance()
    private var nowYear = cal.get(Calendar.YEAR)
    private var nowMonth = cal.get(Calendar.MONTH)
    private var nowDay = cal.get(Calendar.DAY_OF_MONTH)
    private var nowHour = cal.get(Calendar.HOUR_OF_DAY)
    private var nowMinute = cal.get(Calendar.MINUTE)
    private val calendar = Calendar.Builder()
        .set(Calendar.YEAR, nowYear)
        .set(Calendar.MONTH, nowMonth)
        .set(Calendar.DAY_OF_MONTH, nowDay)
        .set(Calendar.HOUR, nowHour)
        .set(Calendar.MINUTE, nowMinute)
        .build()
    private var date = Timestamp(calendar.time)
    private val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.apply {
            newExpenditure = getParcelable(EXPENDITURE_KEY) ?: return@apply
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_expenditure_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (newExpenditure.id != null) {
            add4_textView.text = getString(R.string.text_updata_dailyreport)
        } else {
            add4_textView.text = getString(R.string.text_add_dailyreport)
        }
        if (newExpenditure.type.isNotEmpty()) {
            type_textView.text = newExpenditure.type
        } else {
            showTypeDialog()
        }

        if (newExpenditure.carId.isEmpty()) {
            carId_textView.text = sharedPreferencesManager.carId
        } else {
            carId_textView.text = newExpenditure.carId
        }

        if (newExpenditure.date != Timestamp(Date())) {
            date = newExpenditure.date
        }
        date_textView.text = simpleDateFormat.format(date.toDate())

        if (newExpenditure.price != Int.MIN_VALUE) {
            price_editText.setText(newExpenditure.price.toString())
        }

        ps_editText.setHorizontallyScrolling(false)
        ps_editText.maxLines = Int.MAX_VALUE
        ps_editText.setText(newExpenditure.ps)

        type_imageButton.setOnClickListener { showTypeDialog() }

        carId_imageButton.setOnClickListener { showCarIdDialog() }

        date_imageButton.setOnClickListener { showDatePickerDialog() }

        finish_button.setOnClickListener {
            if (price_editText.text.isEmpty()) {
                price_editText.error = "請輸入價格"
                return@setOnClickListener
            }

            val type = type_textView.text.toString()
            val carId = carId_textView.text.toString()
            val price = price_editText.text.toString().toInt()
            val ps = ps_editText.text.trim().toString()
            val expenditure = newExpenditure.copy(
                userId = sharedPreferencesManager.stuffId,
                type = type,
                date = date,
                carId = carId,
                price = price,
                ps = ps
            )
            if (newExpenditure.id == null) {
                addExpenditure(expenditure)
            } else {
                setExpenditure(expenditure)
            }
        }

        cancel_button.setOnClickListener {
            parentFragmentManager.popBackStack(null, 1)
        }
    }

    private fun addExpenditure(expenditure: Expenditure) {
        val ref = db.collection(Stuff.DIR_NAME).document(sharedPreferencesManager.stuffId)
            .collection(Expenditure.DIR_NAME).document()
        val addExpenditure = expenditure.copy(id = ref.id)
        ref.set(addExpenditure)
            .addOnSuccessListener {
                setAdminExpenditure(addExpenditure)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error addExpenditure", exception)
            }
    }

    private fun setExpenditure(expenditure: Expenditure) {
        db.collection(Stuff.DIR_NAME).document(sharedPreferencesManager.stuffId)
            .collection(Expenditure.DIR_NAME)
            .document(expenditure.id ?: return)
            .set(expenditure)
            .addOnSuccessListener {
                setAdminExpenditure(expenditure)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error setExpenditure", exception)
            }
    }

    private fun setAdminExpenditure(expenditure: Expenditure){
        db.collection(Expenditure.DIR_NAME)
            .document(expenditure.id ?: return)
            .set(expenditure)
            .addOnSuccessListener {
                parentFragmentManager.popBackStack(null, 1)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error setExpenditure", exception)
            }
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        this.context?.let { it1 ->
            DatePickerDialog(it1, { _, year, month, day ->
                kotlin.run {
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
                    date_textView.text = showDate
                }
            }, year, month, day).show()
        }
    }

    private fun showTypeDialog() {
        val types = arrayOf("油錢", "保養")
        val builder = AlertDialog.Builder(this.context)
        with(builder) {
            setTitle("請選擇類型")
            setCancelable(false)
            setSingleChoiceItems(types, -1) { dialog, which ->
                type_textView.text = types[which]
                dialog.dismiss()
            }
            show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.setGroupVisible(0, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val carId = data?.getStringExtra("carId")
            carId_textView.text = carId
        }
    }

    companion object {
        private const val EXPENDITURE_KEY = "expenditure"
        fun newInstance(expenditure: Expenditure? = Expenditure()): ExpenditureEditFragment {
            return ExpenditureEditFragment().apply {
                arguments = Bundle().also { bundle ->
                    bundle.putParcelable(EXPENDITURE_KEY, expenditure)
                }
            }
        }
    }
}