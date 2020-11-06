package com.juan.pinya.view.main.dailyReport

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.juan.pinya.R
import com.juan.pinya.di.SHARED_PREFERENCES_NAME
import com.juan.pinya.extention.setTodayStartTime
import com.juan.pinya.model.DailyReport
import com.juan.pinya.model.Stuff
import com.juan.pinya.module.SharedPreferencesManager
import com.juan.pinya.module.dailyReport.BaseFragment
import com.juan.pinya.module.swipe.DailyReportSwipeHelper
import com.juan.pinya.module.swipe.DeleteButton
import com.juan.pinya.view.main.dailyReport.company.CompanyFragment
import com.juan.pinya.view.main.dailyReport.edit.DailyReportEditFragment
import kotlinx.android.synthetic.main.carid_alertdialog.view.*
import kotlinx.android.synthetic.main.fragment_dailyreport.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class DailyReportFragment : BaseFragment() {
    private val dailyReportViewModel by viewModel<DailyReportViewModel>()
    private val sharedPreferencesManager by inject<SharedPreferencesManager>(SHARED_PREFERENCES_NAME)
    private val calendar = Calendar.getInstance().setTodayStartTime()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val adapter: DailyReportAdapter by lazy {
        DailyReportAdapter(dailyReportViewModel.getDailyReportQuery(calendar.time)) { dailyReport ->
            val fragment = DailyReportEditFragment.newInstance(dailyReport)
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.dailyReport_constraintLayout, fragment, fragment.tag)
                addToBackStack(null)
                commit()
            }
        }
    }

    //getName
    private val userName: String = sharedPreferencesManager.name
    private val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //show title menu
        setHasOptionsMenu(true)
        showCarId()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_dailyreport, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userName_TextView.text = userName
        carId_TextView.text = sharedPreferencesManager.carId
        date_textView.text = simpleDateFormat.format(calendar.time)
        setUpRv()

        carId_imageButtom.setOnClickListener {
            showCarIdDialog(false)
        }

        data_imageButton.setOnClickListener {
            showDatePickerDialog(date_textView)
        }
    }

    private fun showCarId() {
        super.onResume()
        val carId = sharedPreferencesManager.carId
        if (carId.isNotEmpty()) {
            val alertDialogItem =
                LayoutInflater.from(this.context).inflate(R.layout.carid_alertdialog, null)
            AlertDialog.Builder(this.context)
                .setView(alertDialogItem)
                .setPositiveButton(R.string.text_enter, null)
                .setNegativeButton(R.string.text_change) { _, _ -> showCarIdDialog(false) }
                .setCancelable(false)
                .show()
            alertDialogItem.carId_alertDialog_textView.text = carId
        } else {
            showCarIdDialog(false)
        }
    }

    private fun setUpRv() {
        dailyreport_recylerView.layoutManager = LinearLayoutManager(this.context)
        dailyreport_recylerView.adapter = adapter
        val swipe =
            object : DailyReportSwipeHelper(requireContext(), dailyreport_recylerView, 300) {
                override fun instantiateMyButton(
                    dailyReport: DailyReport,
                    buffer: MutableList<DeleteButton>
                ) {
                    buffer.add(DeleteButton(requireContext(),
                        getString(R.string.text_delete),
                        100,
                        0,
                        ContextCompat.getColor(requireContext(),
                            R.color.background_delete_button)) {
                        //禁用所有recycelview控制
//                        dailyreport_recylerView.suppressLayout(true)
                        val dialog = showDeleteDialog()
                        db.collection(Stuff.DIR_NAME)
                            .document(sharedPreferencesManager.stuffId)
                            .collection(DailyReport.DIR_NAME)
                            .document(dailyReport.id ?: return@DeleteButton)
                            .delete()
                            .addOnSuccessListener {
                                deleteAdminDailyReport(dailyReport, dialog)
                            }
                            .addOnFailureListener {
                                dialog.dismiss()
                                Toast.makeText(context,
                                    getString(R.string.text_delete_fail),
                                    Toast.LENGTH_SHORT).show()
//                                dailyreport_recylerView.suppressLayout(false)

                            }
                    })
                }
            }
    }

    private fun deleteAdminDailyReport(dailyReport: DailyReport, dialog: Dialog) {
        db.collection(DailyReport.DIR_NAME)
            .document(dailyReport.id ?: return)
            .delete()
            .addOnSuccessListener {
                dialog.dismiss()
//                dailyreport_recylerView.suppressLayout(false)
                Toast.makeText(context,
                    getString(R.string.text_delete_success),
                    Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                dialog.dismiss()
//                dailyreport_recylerView.suppressLayout(false)
                Toast.makeText(context,
                    getString(R.string.text_delete_admin_fail),
                    Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_action -> {
                showFragment(CompanyFragment.newInstance())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showFragment(fragment: Fragment) {
        val manager: FragmentManager? = parentFragmentManager
        manager?.beginTransaction()?.apply {
            add(R.id.dailyReport_constraintLayout, fragment, fragment.tag)
            addToBackStack(null)
            commit()
        }
    }

    private fun showDatePickerDialog(textView: TextView) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(requireContext(), { _, year, month, day ->
            val showDate = "$year/${month + 1}/$day"
            textView.text = showDate
            calendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
            }
            adapter.updateOptions(dailyReportViewModel.getDailyReportQuery(calendar.time))
        }, year, month, day).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val carId = data?.getStringExtra("carId")
            sharedPreferencesManager.carId = carId as String
            carId_TextView.text = carId
        }
    }

    companion object {
        const val CAR_ID_REQUEST = 1
    }
}





