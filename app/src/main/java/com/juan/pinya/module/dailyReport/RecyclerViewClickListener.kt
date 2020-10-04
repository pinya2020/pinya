package com.juan.pinya.module.dailyReport

import android.view.View
import com.juan.pinya.model.DailyReport

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClick(view: View, dailyReport: DailyReport)
}