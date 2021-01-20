package com.juan.pinya.view.main.monthlyStatistics

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.juan.pinya.R
import com.juan.pinya.di.SHARED_PREFERENCES_NAME
import com.juan.pinya.model.Expenditure
import com.juan.pinya.model.Statistics
import com.juan.pinya.model.Stuff
import com.juan.pinya.module.SharedPreferencesManager
import com.juan.pinya.view.main.expenditure.ExpenditureAdapter
import kotlinx.android.synthetic.main.fragment_expenditure.*
import kotlinx.android.synthetic.main.fragment_monthly_statistics2.*
import org.koin.android.ext.android.inject
import java.util.*

class MonthlyStatisticsFragment2 : Fragment() {
    private lateinit var _statistics: Statistics
    private val sharedPreferencesManager by inject<SharedPreferencesManager>(SHARED_PREFERENCES_NAME)
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val adapter: ExpenditureAdapter by lazy {
        ExpenditureAdapter(getOptions(_statistics.calender)) { expenditure ->
//            val fragment = ExpenditureEditFragment.newInstance(expenditure)
//            parentFragmentManager.beginTransaction().apply {
//                replace(R.id.expenditure_constraintLayout, fragment, fragment.tag)
//                addToBackStack(null)
//                commit()
//            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        arguments?.apply {
            _statistics = getParcelable(STATISTICS_KEY) ?: return@apply
        }

//        (activity as AppCompatActivity).setTitle(_statistics.company)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monthly_statistics2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        monthly_statistics2_rv.layoutManager = LinearLayoutManager(this.context)
        monthly_statistics2_rv.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        (activity as AppCompatActivity).setTitle(R.string.text_monthly_statistics)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.setGroupVisible(0, false)
    }

    private fun getOptions(calendar: Calendar): FirestoreRecyclerOptions<Expenditure> {
        val todayMount = calendar.time
        calendar.add(Calendar.MONTH, 1)
        val tomorrowMount = calendar.time
        calendar.add(Calendar.MONTH, -1)
        val expenditureRef: CollectionReference =
            db.collection(Stuff.DIR_NAME)
                .document(sharedPreferencesManager.stuffId)
                .collection(Expenditure.DIR_NAME)
        val query: Query = expenditureRef
            .whereEqualTo("type", _statistics.company)
            .whereGreaterThanOrEqualTo("date", Timestamp(todayMount))
            .whereLessThan("date", Timestamp(tomorrowMount))


        return FirestoreRecyclerOptions.Builder<Expenditure>()
            .setQuery(query, Expenditure::class.java)
            .build()
    }

    companion object {
        private const val STATISTICS_KEY = "statistics"
        fun newInstance(statistics: Statistics? = Statistics()): MonthlyStatisticsFragment2 {
            return MonthlyStatisticsFragment2().apply {
                arguments = Bundle().also { bundle ->
                    bundle.putParcelable(STATISTICS_KEY, statistics)
                }
            }
        }
    }
}
