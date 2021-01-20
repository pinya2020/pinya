package com.juan.pinya.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName
import com.juan.pinya.extention.setMountStarTime
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Statistics(

    @SerializedName("carId")
    val carId: String = "",

    @SerializedName("company")
    val company: String = "",

    @SerializedName("meter")
    var meter: Int = 0,

    @SerializedName("expenditure")
    var expenditure: Boolean = false,

    @SerializedName("calender")
    var calender: Calendar = Calendar.getInstance().setMountStarTime()

    ) : Parcelable {

    companion object {

    }

}