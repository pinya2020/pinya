package com.juan.pinya.view.main.dailyReport

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.juan.pinya.R
import com.juan.pinya.model.DailyReport

class DailyReportAdapter(
    options: FirestoreRecyclerOptions<DailyReport>,
    private var statistics: Boolean,
    private val listener: (DailyReport) -> Unit
) :
    FirestoreRecyclerAdapter<DailyReport, DailyReportViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyReportViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_dailyreport, parent, false)
        return DailyReportViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: DailyReportViewHolder,
        position: Int,
        dailyReport: DailyReport
    ) {
        holder.onBind(dailyReport, listener, statistics)
    }
}