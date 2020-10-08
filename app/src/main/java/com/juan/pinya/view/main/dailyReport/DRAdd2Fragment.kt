package com.juan.pinya.view.main.dailyReport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.juan.pinya.R
import com.juan.pinya.module.dailyReport.DRAdd3Adapter
import com.juan.pinya.model.DailyReport
import com.juan.pinya.module.dailyReport.DRAdd2Adapter
import com.juan.pinya.module.dailyReport.RecyclerViewClickListener
import kotlinx.android.synthetic.main.fragment_daily_report_add2.*

class DRAdd2Fragment : Fragment(), RecyclerViewClickListener {
    private var adapter: DRAdd2Adapter? = null
    private var companyId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_report_add2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        companyId = arguments?.getString(COMPANY_ID_KEY)
        if (companyId != null) {
            setUpRv(companyId!!)
        }
        add2_up_Button.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        add2_next_Button.setOnClickListener {
            showFragment()

        }
    }

    private fun setUpRv(companyId: String) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dailyReportRef: CollectionReference =
            db.collection("廠商").document(companyId)
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

    private fun showFragment() {
        val fragment = DailyReportAdd3Fragment()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.dailyReport_ConstraintLayout, fragment, fragment.tag)
                addToBackStack(null)
                commit()
            }
    }

    companion object {
        private const val COMPANY_ID_KEY = "companyId"
        fun newInstance(companyId: String): DRAdd2Fragment {
            return DRAdd2Fragment().apply {
                arguments = Bundle().also { bundle ->
                    bundle.putString(COMPANY_ID_KEY, companyId)
                }
            }
        }
    }
}

