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
import kotlinx.android.synthetic.main.fragment_daily_report_add2.*

class DRAdd2Fragment : Fragment(), RecyclerViewClickListener {
    private var adapter: DRAdd2Adapter? = null

    //    private var compostId = arguments?.getString("input_text")
    private var compostId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_report_add2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compostId = arguments?.getString("input_text")
        if (compostId != null) {
            setUpRv(compostId!!)
        }
        add2_up_Button.setOnClickListener {
            showFragment()
        }
        add2_next_Button.setOnClickListener {
            showFragment()

        }
    }

    private fun setUpRv(compostId: String) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dailyReportRef: CollectionReference =
            db.collection("廠商").document(compostId)
                .collection("工地")

        val query: Query = dailyReportRef
        val options = FirestoreRecyclerOptions.Builder<DailyReport>()
            .setQuery(query, DailyReport::class.java)
            .build()

        adapter = DRAdd2Adapter(options, this)
        rv_add2.layoutManager = LinearLayoutManager(this.context)
        rv_add2.adapter = adapter
    }

    override fun onRecyclerViewItemClick(view: View, dailyReport: DailyReport) {
        add2_textView.text = dailyReport.site
    }

    override fun onStart() {
        super.onStart()
        if (adapter != null) {
            adapter!!.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        if (adapter != null) {
            adapter!!.stopListening()
        }
    }

    fun showFragment() {
        val fragment = DailyReportAdd3Fragment()
        val manager: FragmentManager? = getFragmentManager()
        manager!!.beginTransaction().replace(
            R.id.dailyReport_ConstraintLayout,
            fragment, fragment.tag
        ).commit()
    }

}

