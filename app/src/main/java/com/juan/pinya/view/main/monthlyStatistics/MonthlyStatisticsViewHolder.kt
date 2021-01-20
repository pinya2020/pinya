package com.juan.pinya.view.main.monthlyStatistics

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_monthly_statistics.view.*

class MonthlyStatisticsViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val companyTextView = itemView.s_company_textView
    val meterTextView = itemView.s_meter_textView
}