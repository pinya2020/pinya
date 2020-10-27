package com.juan.pinya.model.stuff

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.juan.pinya.model.DailyReport
import java.util.*

interface StuffRepository {

    fun getDailyReportOptions(stuffId: String, date: Date): FirestoreRecyclerOptions<DailyReport>
}