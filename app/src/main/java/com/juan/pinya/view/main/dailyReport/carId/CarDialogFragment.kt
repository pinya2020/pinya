package com.juan.pinya.view.main.dailyReport.carId

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.juan.pinya.R
import com.juan.pinya.di.SHARED_PREFERENCES_NAME
import com.juan.pinya.model.Car
import com.juan.pinya.module.SharedPreferencesManager
import com.juan.pinya.module.dailyReport.CarDialogClickListener
import kotlinx.android.synthetic.main.carid_dialog.*
import org.koin.android.ext.android.inject

class CarDialogFragment : DialogFragment(), CarDialogClickListener {
    private val sharedPreferencesManager by inject<SharedPreferencesManager>(SHARED_PREFERENCES_NAME)
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val carAdapter: CarIdAdapter by lazy {
        val dailyReportRef: CollectionReference =
            db.collection(Car.DIR_NAME)
        val showAll = arguments?.getBoolean("showAll")
        val query: Query
        if (showAll!!){
            query = dailyReportRef
        }else{
            query = dailyReportRef.whereNotEqualTo("carId","ALL")
        }
        val options = FirestoreRecyclerOptions.Builder<Car>()
            .setQuery(query, Car::class.java)
            .build()
        CarIdAdapter(options, this)
    }
    private var carId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.carid_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isCancelable = false
        view.setBackgroundResource(R.color.pinya_yellow1)
        setUpCar()
        carId_dialog_Button.setOnClickListener {
            if (carId == null){
                carId_dialog_textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.delete))
                return@setOnClickListener
            }
            dismiss()
        }
    }

    fun setUpCar() {
        carId_dialog_rv.layoutManager = LinearLayoutManager(context)
        carId_dialog_rv.adapter = carAdapter
    }

    override fun onRecyclerViewItemClick(view: View, car: Car) {
        carId_dialog_textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.mr_cast_meta_black_scrim))
        carId = car.carId
    }

    override fun onStart() {
        super.onStart()
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        dialog?.window?.attributes = dialog?.window?.attributes?.apply {
            height = metrics.heightPixels * 8 / 10
            width = metrics.widthPixels * 8 / 10
        }
        carAdapter.startListening()
    }

    override fun onStop() {
        carAdapter.stopListening()
        super.onStop()
    }

    override fun onDismiss(dialog: DialogInterface) {
        val intent = Intent()
        intent.putExtra("carId", carId)
        targetFragment?.onActivityResult(targetRequestCode, RESULT_OK, intent)
        super.onDismiss(dialog)
    }
}