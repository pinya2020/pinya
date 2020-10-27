package com.juan.pinya.model.stuff

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.juan.pinya.extention.getTomorrowDate
import com.juan.pinya.model.DailyReport
import java.util.*

class StuffRepositoryImpl(
    private val stuffCollection: CollectionReference
) : StuffRepository {

    override fun getDailyReportOptions(stuffId: String, date: Date): FirestoreRecyclerOptions<DailyReport> {
        val query = getDailyReportCollection(stuffId)
            .whereGreaterThanOrEqualTo(DailyReport.DATE_KEY, Timestamp(date))
            .whereLessThan(DailyReport.DATE_KEY, Timestamp(date.getTomorrowDate()))
        return FirestoreRecyclerOptions.Builder<DailyReport>()
            .setQuery(query, DailyReport::class.java)
            .build()
    }

    private fun getDailyReportCollection(stuffId: String): CollectionReference {
        return stuffCollection.document(stuffId)
            .collection(DailyReport.DIR_NAME)
    }
}