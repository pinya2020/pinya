package com.juan.pinya.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Site(

    @SerializedName("siteId")
    val siteId: String = "",

    @SerializedName("site")
    val site: String = ""
) : Parcelable {

    companion object {
        const val DIR_NAME = "工地"
    }
}