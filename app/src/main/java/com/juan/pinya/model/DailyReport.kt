package com.juan.pinya.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class DailyReport(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("address")
    val address: String = "",

    @SerializedName("carId")
    val carId: String = "",

    @SerializedName("company")
    val company: String = "",

    @SerializedName(DATE_KEY)
    val date: Timestamp = Timestamp(Date()),

    @SerializedName("meter")
    val meter: Int = Int.MIN_VALUE,

    @SerializedName(USER_NAME_KEY)
    val userName: String = "",

    @SerializedName(USER_ID_KEY)
    val userId: String = "",


    @SerializedName("number")
    val number: Int = Int.MIN_VALUE,

    @SerializedName("price")
    val price: Int = Int.MIN_VALUE,

    @SerializedName("ps")
    val ps: String = "",

    @SerializedName("site")
    val site: String = "",

    @SerializedName("companyId")
    val companyId: String = "",

    @SerializedName("siteId")
    val siteId: String = "",

    @SerializedName("addressId")
    val addressId: String = ""
) : Parcelable {

    companion object {
        const val DIR_NAME = "日報表"
        const val DATE_KEY = "date"
        const val USER_NAME_KEY = "userName"
        const val USER_ID_KEY = "userId"
    }
}

