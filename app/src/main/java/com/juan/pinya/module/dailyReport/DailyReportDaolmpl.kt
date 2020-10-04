package com.juan.pinya.module.dailyReport

import android.content.ContentValues.TAG
import android.telecom.Call
import android.util.Log
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.juan.pinya.model.DailyReport
import com.juan.pinya.model.Stuff
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DailyReportDaolmpl(private val gson: Gson) : DailyReportDao {
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }


    override suspend fun getDailyReportsById(id: String, year: String, month: String)
        {
            val continuation =
            firestore.collection(Stuff.DIR_NAME)
                .document(id)
                .collection("年")
                .document(year)
                .collection("月")
                .document(month)
                .collection(DailyReport.DIR_NAME)
                .get()
                .addOnSuccessListener {
                }
        }

    private inline fun <reified T> DocumentSnapshot.parseDocument(): T {
        val jsonString = gson.toJson(data)
        return gson.fromJson(jsonString, object : TypeToken<T>() {}.type)
    }
}

