package com.juan.pinya.module

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.juan.pinya.model.Stuff
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class StuffDaoImpl(private val gson: Gson) : StuffDao {
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override suspend fun getStuffById(id: String) =
        suspendCoroutine<Result<Stuff>> { continuation ->
            firestore.collection(Stuff.DIR_NAME)
                .document(id)
                .get()
                .addOnSuccessListener {
                    continuation.resume(Result.success(it.parseDocument()))
                }
                .addOnFailureListener {
                    continuation.resume(Result.failure(it))
                }
        }

    private inline fun <reified T> DocumentSnapshot.parseDocument(): T {
        val jsonString = gson.toJson(data)
        return gson.fromJson(jsonString, object : TypeToken<T>() {}.type)
    }
}