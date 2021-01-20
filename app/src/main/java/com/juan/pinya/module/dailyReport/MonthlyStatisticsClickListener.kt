package com.juan.pinya.module.dailyReport

import android.view.View
import com.juan.pinya.model.Statistics

interface MonthlyStatisticsClickListener {
    fun onRecyclerViewItemClick(view: View, statistics: Statistics)

}