package com.juan.pinya.view.main.dailyReport.site

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.juan.pinya.R
import com.juan.pinya.model.Company
import com.juan.pinya.model.DailyReport
import com.juan.pinya.model.Site
import com.juan.pinya.module.dailyReport.BaseFragment
import com.juan.pinya.module.dailyReport.SiteClickListener
import com.juan.pinya.view.main.dailyReport.address.AddressFragment
import kotlinx.android.synthetic.main.fragment_daily_report_add2.*

class SiteFragment : BaseFragment(), SiteClickListener {
    private var adapter: SiteAdapter? = null
    private lateinit var newDailyReport: DailyReport
    override var bottomNavigationViewVisibility = View.GONE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        newDailyReport = arguments?.getParcelable(DAILY_REPORT_KEY) ?: return
        newDailyReport = newDailyReport.copy(siteId = "", site = "", addressId = "", address = "")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_report_add2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRv(newDailyReport.companyId)
        add2_up_Button.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        add2_next_Button.setOnClickListener {

            if (newDailyReport.siteId.isNotEmpty()&&newDailyReport.site.isNotEmpty()){
                showFragment(newDailyReport)
            }else{
                add2_textView.setTextColor(ContextCompat.getColor(requireContext(),
                    R.color.background_delete_button))
            }

        }
    }

    private fun setUpRv(companyId: String) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dailyReportRef: CollectionReference =
            db.collection(Company.DIR_NAME).document(companyId)
                .collection(Site.DIR_NAME)

        val query: Query = dailyReportRef
        val options = FirestoreRecyclerOptions.Builder<Site>()
            .setQuery(query, Site::class.java)
            .build()

        adapter = SiteAdapter(options, this)
        rv_add2.layoutManager = LinearLayoutManager(this.context)
        rv_add2.adapter = adapter
    }

    override fun onRecyclerViewItemClick(view: View, site: Site) {
        add2_textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.pinya_system_black))
        newDailyReport = newDailyReport.copy(
            site = site.site,
            siteId = site.siteId
        )
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

    private fun showFragment(dailyReport: DailyReport) {
        val fragment = AddressFragment.newInstance(dailyReport)
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.dailyReport_constraintLayout, fragment, fragment.tag)
            addToBackStack(null)
            commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.setGroupVisible(0,false)
    }

    companion object {
        private const val DAILY_REPORT_KEY = "dailyReport"
        fun newInstance(dailyReport: DailyReport): SiteFragment {
            return SiteFragment().apply {
                arguments = Bundle().also { bundle ->
                    bundle.putParcelable(DAILY_REPORT_KEY, dailyReport)
                }
            }
        }
    }
}

