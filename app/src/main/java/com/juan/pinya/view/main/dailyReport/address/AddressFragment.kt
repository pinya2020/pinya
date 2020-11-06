package com.juan.pinya.view.main.dailyReport.address

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.juan.pinya.R
import com.juan.pinya.model.Address
import com.juan.pinya.model.DailyReport
import com.juan.pinya.module.dailyReport.AddressClickListener
import com.juan.pinya.module.dailyReport.BaseFragment
import com.juan.pinya.view.main.dailyReport.edit.DailyReportEditFragment
import kotlinx.android.synthetic.main.fragment_daily_report_add3.*

class AddressFragment: BaseFragment(), AddressClickListener {
    private var adapter: AddressAdapter? = null
    private lateinit var newDailyReport: DailyReport
    override var bottomNavigationViewVisibility = View.GONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.apply {
            newDailyReport = getParcelable(DAILY_REPORT_KEY) ?: return@apply
        }
        newDailyReport = newDailyReport.copy(addressId = "", address = "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_report_add3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRv(newDailyReport.companyId, newDailyReport.siteId)
        add3_up_Button.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        add3_next_Button.setOnClickListener {
            if (newDailyReport.addressId.isNotEmpty()&&newDailyReport.address.isNotEmpty()){
                showFragment(newDailyReport)
            }else{
                add3_textView.setTextColor(ContextCompat.getColor(requireContext(),
                    R.color.background_delete_button))
            }

        }
    }

    private fun setUpRv(companyId: String, siteId: String) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dailyReportRef: CollectionReference =
            db.collection("廠商").document(companyId)
                .collection("工地").document(siteId)
                .collection("地點")

        val query: Query = dailyReportRef
        val options = FirestoreRecyclerOptions.Builder<Address>()
            .setQuery(query, Address::class.java)
            .build()

        adapter = AddressAdapter(options, this)
        rv_add3.layoutManager = LinearLayoutManager(this.context)
        rv_add3.adapter = adapter
    }

    override fun onRecyclerViewItemClick(view: View, address: Address) {
        add3_textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.pinya_system_black))

        newDailyReport = newDailyReport.copy(
            address = address.address,
            addressId = address.addressId,
            price = address.cost
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
        val fragment = DailyReportEditFragment.newInstance(dailyReport)
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
        fun newInstance(dailyReport: DailyReport): AddressFragment {
            return AddressFragment().apply {
                arguments = Bundle().also { bundle ->
                    bundle.putParcelable(DAILY_REPORT_KEY, dailyReport)
                }
            }
        }
    }
}