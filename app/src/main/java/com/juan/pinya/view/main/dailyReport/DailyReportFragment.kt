package com.juan.pinya.view.main.dailyReport

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.juan.pinya.R
import com.juan.pinya.di.SHARED_PREFERENCES_NAME
import com.juan.pinya.model.DailyReport
import com.juan.pinya.model.Stuff
import com.juan.pinya.module.SharedPreferencesManager
import com.juan.pinya.module.dailyReport.DailyReportAdapter
import kotlinx.android.synthetic.main.fragment_dailyreport.*
import java.util.*
import com.juan.pinya.module.dailyReport.RecyclerViewClickListener
import org.koin.android.ext.android.inject


class DailyReportFragment : Fragment(), RecyclerViewClickListener {
    private var adapter: DailyReportAdapter? = null
    private val cal = Calendar.getInstance()
    private var nowYear: Int = cal.get(Calendar.YEAR)
    private var nowMonth: Int = cal.get(Calendar.MONTH)
    private var nowDay: Int = cal.get(Calendar.DAY_OF_MONTH)
    private val sharedPreferencesManager by inject<SharedPreferencesManager>(SHARED_PREFERENCES_NAME)

    //getname
    private val userName: String = sharedPreferencesManager.name

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
        //toolbar
        setHasOptionsMenu(true)
        userName_TextView.text = userName
        date_textView.text = "$nowYear/${nowMonth+1}/$nowDay"
        setUpRv(nowYear, nowMonth, nowDay)

        carId_imageButtom.setOnClickListener {
        }

        data_imageButton.setOnClickListener {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            this.context?.let { it1 ->
                DatePickerDialog(it1, { _, year, month, day ->
                    kotlin.run {
                        val showDate = "$year/${month+1}/$day"
                        date_textView.text = showDate
                        adapter?.stopListening()
                        setUpRv(year, month, day)
                        adapter?.startListening()

                    }
                }, year, month, day).show()
            }
        }
    }

    private fun setUpRv(year: Int, month: Int, day: Int) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val calendar = Calendar.Builder()
            .set(Calendar.YEAR, year)
            .set(Calendar.MONTH, month)
            .set(Calendar.DAY_OF_MONTH, day)
            .build()
        val dailyReportRef: CollectionReference =
            db.collection(Stuff.DIR_NAME)
                .document(sharedPreferencesManager.id)
                .collection(DailyReport.DIR_NAME)
        val query: Query = dailyReportRef.whereEqualTo("date", Timestamp(calendar.time))
        val options = FirestoreRecyclerOptions.Builder<DailyReport>()
            .setQuery(query, DailyReport::class.java)
            .build()

        adapter = DailyReportAdapter(options, this)
        dailyreport_recylerView.layoutManager = LinearLayoutManager(this.context)
        dailyreport_recylerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_action -> {
                showFragment(DRAdd1Fragment())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showFragment(fragment: Fragment) {
        val manager: FragmentManager? = parentFragmentManager
        manager?.beginTransaction()?.apply {
            add(R.id.dailyReport_ConstraintLayout, fragment, fragment.tag)
            addToBackStack(null)
            commit()
        }
    }

    override fun onRecyclerViewItemClick(view: View, dailyReport: DailyReport) {
        showFragment(DailyReportAdd4Fragment())
    }
}



