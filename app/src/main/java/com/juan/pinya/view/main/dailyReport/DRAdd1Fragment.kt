package com.juan.pinya.fragments.dailyreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.juan.pinya.R
import com.juan.pinya.model.DailyReport
import com.juan.pinya.module.dailyReport.RecyclerViewClickListener
import kotlinx.android.synthetic.main.fragment_daily_report_add1.*

class DRAdd1Fragment : Fragment(), RecyclerViewClickListener {
    private var adapter: DRAdd1Adapter? = null
    private var newDailyReport = DailyReport()
    lateinit var comm: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_report_add1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        comm = activity as Communicator
        setUpRv()
        add1_cancel_button.setOnClickListener{
            parentFragmentManager.popBackStack()
        }
        add1_next_Button.setOnClickListener{
            showFragment(newDailyReport.companyId.toString())
//            val intent = Intent(this.context, DailyReportAdd2Fragment::class.java)
//            intent.putExtra("compost", newDailyReport?.company.toString())
//            intent.putExtra("compostId", newDailyReport?.companyId.toString())
        }
    }

    private fun setUpRv() {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dailyReportRef: CollectionReference = db.collection("廠商")

        val query: Query = dailyReportRef
        val options = FirestoreRecyclerOptions.Builder<DailyReport>()
            .setQuery(query, DailyReport::class.java)
            .build()

        adapter = DRAdd1Adapter(options, this)
        rv_add1.layoutManager = LinearLayoutManager(this.context)
        rv_add1.adapter = adapter
    }
    override fun onRecyclerViewItemClick(view: View, dailyReport: DailyReport) {
        add1_textView.text = dailyReport.company
        newDailyReport.companyId = dailyReport.id
        println("string ${newDailyReport.companyId.toString()}")
        newDailyReport.company = dailyReport.company
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    private fun showFragment(compostId: String) {
        val fragment = DRAdd2Fragment.newInstance(compostId)
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.dailyReport_ConstraintLayout, fragment, fragment.tag)
            addToBackStack(null)
            commit()
        }
    }
}

