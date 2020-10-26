package com.juan.pinya.view.main.dailyReport

import android.content.Context
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.juan.pinya.R
import com.juan.pinya.model.DailyReport
import kotlinx.android.synthetic.main.item_dailyreport.view.*
import java.text.SimpleDateFormat
import java.util.*

class DailyReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var dailyReport: DailyReport? = null
    private val format = SimpleDateFormat("HH:mm", Locale.getDefault())

    private val context: Context
        get() = itemView.context
    private val addressTextView = itemView.address_textView
    private var companyTextView = itemView.company_textView
    private val dataTextView = itemView.itemDate_textView
    private val meterTextView = itemView.meter_textView
    private val numberTextView = itemView.numder_textView
    private val priceTextView = itemView.price_textView
    private val psTextView = itemView.ps_textView
    private var siteTextView = itemView.site_textView

    fun onBind(dailyReport: DailyReport, listener: (DailyReport) -> Unit) {
        this.dailyReport = dailyReport
        addressTextView.text = dailyReport.address
        companyTextView.text = dailyReport.company
        dataTextView.text = format.format(dailyReport.date.toDate())
        meterTextView.text = String.format(Locale.getDefault(), context.getString(R.string.format_meter), dailyReport.meter)
        numberTextView.text = String.format(Locale.getDefault(), context.getString(R.string.format_number), dailyReport.number)
        priceTextView.text = String.format(Locale.getDefault(), context.getString(R.string.format_price), dailyReport.price)

        if (dailyReport.ps.isNotEmpty()){
            psTextView.visibility = View.VISIBLE
            psTextView.text = dailyReport.ps
        }else{
            psTextView.visibility = View.GONE
        }
        siteTextView.text = dailyReport.site

        itemView.setOnLongClickListener {
            //???
            listener.invoke(dailyReport)
            true
        }
    }
}