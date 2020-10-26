package com.juan.pinya.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Expenditure(

    @SerializedName("id")
    val id: String ?= null,

    @SerializedName("userId")
    val userId: String = "",

    @SerializedName("price")
    val price: Int = Int.MIN_VALUE,

    @SerializedName("date")
    val date: Timestamp = Timestamp(Date()),

    @SerializedName("carId")
    val carId: String = "",

    @SerializedName("ps")
    val ps: String = "",

    @SerializedName("type")
    val type: String = ""
) : Parcelable {

    companion object {
        const val DIR_NAME = "支出表"
    }
}