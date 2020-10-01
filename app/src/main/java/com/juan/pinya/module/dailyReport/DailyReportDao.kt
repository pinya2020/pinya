package com.juan.pinya.module.dailyReport

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.juan.pinya.model.DailyReport
import com.juan.pinya.model.Stuff

interface DailyReportDao {
    suspend fun getDailyReportsById( id: String, year: String, month: String)
}