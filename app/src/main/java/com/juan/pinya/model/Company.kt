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

    @SerializedName("endOfMount")
    val endOfMount: Boolean = false,

    @SerializedName("endDate")
    val endDate: Int = ZERO_KEY,

    @SerializedName("ininvoiceTaxRate")
    val ininvoiceTaxRate: Double = TAX_RATE_KEY,

    @SerializedName("checkTaxRate")
    val checkTaxRate: Double = TAX_RATE_KEY

) : Parcelable {

    companion object {

        const val DIR_NAME = "廠商"
        const val COMPANY_KEY = "company"
        const val TAX_RATE_KEY = 1.0
        const val ZERO_KEY = 0
    }
}