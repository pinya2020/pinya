package com.juan.pinya.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Site(

    @SerializedName("siteId")
    val siteId: String = "",

    @SerializedName(SITE_KEY)
    val site: String = "",

    @SerializedName("date")
    val date: Timestamp = Timestamp(Date())

) : Parcelable {

    companion object {
        const val DIR_NAME = "工地"
        const val SITE_KEY = "site"
    }
}