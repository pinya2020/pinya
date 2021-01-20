package com.juan.pinya.view.main.dailyReport.company

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.juan.pinya.R
import com.juan.pinya.di.SHARED_PREFERENCES_NAME
import com.juan.pinya.model.Company
import com.juan.pinya.model.DailyReport
import com.juan.pinya.module.SharedPreferencesManager
import com.juan.pinya.module.dailyReport.BaseFragment
import com.juan.pinya.module.dailyReport.CompanyClickListener
import com.juan.pinya.view.main.dailyReport.site.SiteFragment
import kotlinx.android.synthetic.main.fragment_daily_report_add1.*
import org.koin.android.ext.android.inject

class CompanyFragment : BaseFragment(), CompanyClickListener {
    private val sharedPreferencesManager by inject<SharedPreferencesManager>(SHARED_PREFERENCES_NAME)
    private val adapter: CompanyAdapter by lazy {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dailyReportRef: CollectionReference = db.collection(Company.DIR_NAME)
        val query: Query = dailyReportRef.orderBy("company")
        val options = FirestoreRecyclerOptions.Builder<Company>()
            .setQuery(query, Company::class.java)
            .build()
        CompanyAdapter(options, this)
    }
    private var newDailyReport = DailyReport()
    override var bottomNavigationViewVisibility = View.GONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val id = arguments?.getString(DAILY_REPORT_KEY)
        newDailyReport = newDailyReport.copy(
            id = id,
            userId = sharedPreferencesManager.stuffId,
            userName = sharedPreferencesManager.name,
            carId = sharedPreferencesManager.carId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_report_add1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //toolbar
        setUpRv()
        add1_cancel_button.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        add1_next_Button.setOnClickListener {
            if (newDailyReport.companyId.isNotEmpty() && newDailyReport.company.isNotEmpty()) {
                showFragment(newDailyReport)
            } else {
                add1_textView.setTextColor(ContextCompat.getColor(requireContext(),
                    R.color.delete))
            }
        }
    }

    private fun setUpRv() {
        rv_add1.layoutManager = LinearLayoutManager(this.context)
        rv_add1.adapter = adapter
    }

    override fun onRecyclerViewItemClick(view: View, company: Company) {
        add1_textView.setTextColor(ContextCompat.getColor(requireContext(),
            R.color.pinya_system_black))
        newDailyReport = newDailyReport.copy(
            company = company.company,
            companyId = company.companyId
        )
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    private fun showFragment(dailyReport: DailyReport) {
        val fragment = SiteFragment.newInstance(dailyReport)
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.dailyReport_constraintLayout, fragment, fragment.tag)
            addToBackStack(null)
            commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.setGroupVisible(0, false)
    }

    companion object {
        private const val DAILY_REPORT_KEY = "dailyReport"
        fun newInstance(id: String? = null): CompanyFragment {
            return CompanyFragment().apply {
                arguments = Bundle().also { bundle ->
                    bundle.putString(DAILY_REPORT_KEY, id)
                }
            }
        }
    }
}

