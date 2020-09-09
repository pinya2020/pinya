package com.juan.pinya.model

import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.annotations.SerializedName

data class Stuff(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: String,
    @SerializedName("password") val password: String
) {
    companion object {
        const val DIR_NAME = "員工"
    }
}