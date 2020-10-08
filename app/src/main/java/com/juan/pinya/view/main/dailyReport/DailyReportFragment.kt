package com.juan.pinya.fragments.dailyreport

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.juan.pinya.R
import com.juan.pinya.model.DailyReport
import com.juan.pinya.model.Stuff
import com.juan.pinya.module.dailyReport.DailyReportAdapter
import kotlinx.android.synthetic.main.fragment_dailyreport.*
import java.util.*
import com.juan.pinya.module.dailyReport.RecyclerViewClickListener


class DailyReportFragment : Fragment(), RecyclerViewClickListener {
    private var adapter: DailyReportAdapter? = null
    private val cal = Calendar.getInstance()
    private var nowYear: Int = cal.get(Calendar.YEAR)
    private var nowMonth: Int = (cal.get(Calendar.MONTH) + 1)


    //如何抓名字
    private val userName: String = "王小明"

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
        setUpRv("123", "$nowYear", "$nowMonth")

        carId_imageButtom.setOnClickListener {
        }

        data_imageButton.setOnClickListener {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            this.context?.let { it1 ->
                DatePickerDialog(it1, { _, year, month, day ->
                    kotlin.run {
                        val thisMonth = month + 1
                        val format = "$year/$thisMonth/$day"
                        date_textView.text = format
                        adapter?.stopListening()
                        setUpRv("123", "$year", "$thisMonth")
                        adapter?.startListening()

                    }
                }, year, month, day).show()
            }
        }
    }

    private fun setUpRv(id: String, year: String, month: String) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val calendar = Calendar.Builder()
            .set(Calendar.YEAR, 2020)
            .set(Calendar.MONTH, 9)
            .set(Calendar.DAY_OF_MONTH, 4)
            .build()
        val dailyReportRef: CollectionReference =
            db.collection(Stuff.DIR_NAME)
                .document(id)
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



