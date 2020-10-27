package com.juan.pinya.view.main.dailyReport

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.juan.pinya.model.DailyReport
import com.juan.pinya.model.SingleLiveEvent
import com.juan.pinya.model.stuff.StuffRepository
import com.juan.pinya.module.SharedPreferencesManager
import java.util.*

class DailyReportViewModel(
    private val stuffRepository: StuffRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    fun getDailyReportQuery(date: Date): FirestoreRecyclerOptions<DailyReport> {
        val stuffId = sharedPreferencesManager.stuffId
        return stuffRepository.getDailyReportOptions(stuffId, date)
    }
}