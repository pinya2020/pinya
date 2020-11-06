package com.juan.pinya.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(

    @SerializedName("companyId")
    val companyId: String = "",

    @SerializedName(COMPANY_KEY)
    val company: String = "",

    @SerializedName("endDate")
    val endDate: String = "",

    @SerializedName("taxRate")
    val taxRate: Double = Double.MIN_VALUE

) : Parcelable {

    companion object {
        const val DIR_NAME = "廠商"
        const val COMPANY_KEY = "company"
    }
}