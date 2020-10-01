package com.juan.pinya.model

import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName

class DailyReport{
    @SerializedName("address")val address: String? = null
    @SerializedName("carId") val carId: String? = null
    @SerializedName("company") var company: String? = null
    @SerializedName("date") val date: Timestamp? = null
    @SerializedName("id") val id: String? = null
    @SerializedName("meter") val meter: Int? = null
    @SerializedName("name") val name: String? = null
    @SerializedName("number") val number: Int? = null
    @SerializedName("price") val price: Int? = null
    @SerializedName("ps") val ps: String? = null
    @SerializedName("site") val site: String? = null
    @SerializedName("companyId") var companyId: String? = null
    @SerializedName("siteId") val siteId: String? = null
    @SerializedName("addressId") val addressId: String? = null

    companion object {
        const val DIR_NAME = "日報表"
    }
}
