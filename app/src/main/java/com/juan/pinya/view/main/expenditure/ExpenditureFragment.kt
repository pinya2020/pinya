package com.juan.pinya.view.main.expenditure

import android.app.DatePickerDialog
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.juan.pinya.extention.setMountStarTime
import com.juan.pinya.model.Expenditure
import com.juan.pinya.model.Stuff
import com.juan.pinya.module.SharedPreferencesManager
import com.juan.pinya.module.swipe.DeleteButton
import com.juan.pinya.module.swipe.ExpenditureSwipeHelper
import kotlinx.android.synthetic.main.fragment_dailyreport.*
import kotlinx.android.synthetic.main.fragment_expenditure.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*


class ExpenditureFragment : Fragment() {
    private val sharedPreferencesManager by inject<SharedPreferencesManager>(SHARED_PREFERENCES_NAME)
    private val calendar = Calendar.getInstance().setMountStarTime()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val adapter: ExpenditureAdapter by lazy {
        ExpenditureAdapter(getOptions(calendar)) { expenditure ->
            val fragment = ExpenditureEditFragment.newInstance(expenditure)
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.expenditure_constraintLayout, fragment, fragment.tag)
                addToBackStack(null)
                commit()
            }
        }
    }
    private lateinit var newExpenditure: Expenditure


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_expenditure, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRv()
        userName_textView.text = sharedPreferencesManager.name
        val formatDate = SimpleDateFormat("yyyy/MM", Locale.getDefault())
        month_textView.text = formatDate.format(calendar.time)
        mount_imageButtom.setOnClickListener {
            val editYear = calendar.get(Calendar.YEAR)
            val editMonth = calendar.get(Calendar.MONTH)
            val editDay = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(requireContext(),
                R.style.DatePickerDialog,
                { _, year, month, day ->
                    val showDate = "$year/${month + 1}"
                    month_textView.text = showDate
                    calendar.apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, day)
                    }
                    adapter.updateOptions(getOptions(calendar))
                },
                editYear,
                editMonth,
                editDay)
            datePickerDialog.getDatePicker().findViewById<View>(Resources.getSystem()
                .getIdentifier("day", "id", "android")).setVisibility(View.GONE)
            datePickerDialog.show()
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }


    private fun setUpRv() {
        expenditure_recylerView.layoutManager = LinearLayoutManager(this.context)
        expenditure_recylerView.adapter = adapter
        val swipe = object : ExpenditureSwipeHelper(requireContext(),
            expenditure_recylerView!!,
            300) {
            override fun instantiateMyButton(
                expenditure: Expenditure,
                buffer: MutableList<DeleteButton>
            ) {

                buffer.add(DeleteButton(requireContext(),
                    getString(R.string.text_delete),
                    100,
                    0,
                    ContextCompat.getColor(requireContext(), R.color.background_delete_button)) {
                    expenditure_recylerView.suppressLayout(true)
                    db.collection(Stuff.DIR_NAME)
                        .document(sharedPreferencesManager.stuffId)
                        .collection(Expenditure.DIR_NAME)
                        .document(expenditure.id ?: return@DeleteButton)
                        .delete()
                        .addOnSuccessListener {
                            deleteAdminExpenditure(expenditure)
                            expenditure_recylerView.suppressLayout(false)
                        }
                        .addOnFailureListener {
                            Toast.makeText(context,
                                R.string.text_delete_fail,
                                Toast.LENGTH_SHORT).show()
                            expenditure_recylerView.suppressLayout(false)
                        }
                })
            }
        }
    }

    private fun deleteAdminExpenditure(expenditure: Expenditure) {
        db.collection(Expenditure.DIR_NAME)
            .document(expenditure.id ?: return)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context,
                    R.string.text_delete_success,
                    Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context,
                    R.string.text_delete_admin_fail,
                    Toast.LENGTH_SHORT).show()
            }
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
            .whereGreaterThanOrEqualTo("date", Timestamp(todayMount))
            .whereLessThan("date", Timestamp(tomorrowMount))

        return FirestoreRecyclerOptions.Builder<Expenditure>()
            .setQuery(query, Expenditure::class.java)
            .build()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_action -> {
                showFragment(ExpenditureEditFragment.newInstance())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showFragment(fragment: Fragment) {
        val manager: FragmentManager? = parentFragmentManager
        manager?.beginTransaction()?.apply {
            add(R.id.expenditure_constraintLayout, fragment, fragment.tag)
            addToBackStack(null)
            commit()
        }
    }
}