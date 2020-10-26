package com.juan.pinya.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(

    @SerializedName("companyId")
    val companyId: String = "",

    @SerializedName("company")
    val company: String = ""
) : Parcelable {

    companion object {
        const val DIR_NAME = "廠商"
    }
}