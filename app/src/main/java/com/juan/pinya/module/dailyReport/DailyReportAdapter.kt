package com.juan.pinya.module.dailyReport

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.juan.pinya.R
import com.juan.pinya.view.main.dailyReport.DailyReportFragment
import com.juan.pinya.model.DailyReport
import kotlinx.android.synthetic.main.item_dailyreport.view.*
import java.text.SimpleDateFormat
import java.util.*

class DailyReportAdapter(
    options: FirestoreRecyclerOptions<DailyReport>,
    private val listener: DailyReportFragment
    ) :
    FirestoreRecyclerAdapter<DailyReport, DailyReportAdapter.DailyReportViewHoder>(options) {

    inner class DailyReportViewHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val address = itemView.address_textView
        var company = itemView.company_textView
        val data = itemView.itemDate_textView
        val meter = itemView.meter_textView
        val number = itemView.numder_textView
        val price = itemView.price_textView
        val ps = itemView.ps_textView
        var site = itemView.site_textView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyReportViewHoder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_dailyreport, parent, false)
        return DailyReportViewHoder(itemView)
    }

    override fun onBindViewHolder(
        holder: DailyReportViewHoder,
        position: Int,
        dailyReport: DailyReport
    ) {
        val format = SimpleDateFormat("hh:mm", Locale.getDefault())
        holder.address.text = dailyReport.address
        holder.company.text = dailyReport.company
        holder.data.text = format.format(dailyReport.date?.toDate() ?: Date())
        holder.meter.text = dailyReport.meter.toString() + " 米"
        holder.number.text = "no."+dailyReport.number.toString()
        holder.price.text = dailyReport.price.toString()
        holder.ps.text = dailyReport.ps
        holder.site.text = dailyReport.site

        holder.itemView.setOnClickListener{
            listener.onRecyclerViewItemClick(holder.itemView, dailyReport)
        }
    }
}