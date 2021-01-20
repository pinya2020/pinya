package com.juan.pinya.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Car(
    @SerializedName("id")
    val id: String = "",

    @SerializedName(CAR_ID_KEY)
    val carId: String = "",

//    @SerializedName("personInCharge")
//    val personInCharge: String = "",

    @SerializedName("loanMonths")
    val loan: Boolean = false,

    @SerializedName("loanAmount")
    val loanAmount: Int = ZERO_KEY,

    @SerializedName("loanMonths")
    val loanMonths: Int = ZERO_KEY,

    @SerializedName("loanPayment")
    val loanPayment: Int = ZERO_KEY,

    @SerializedName("paymentDate")
    val paymentDate: Int = ZERO_KEY,

    @SerializedName("violation")
    var violation: Int = ZERO_KEY,

    @SerializedName("updataDate")
    var updataDate: Timestamp = Timestamp(Date())

) : Parcelable {

    companion object {
        const val DIR_NAME = "車輛"
        const val CAR_ID_KEY = "carId"
        const val ZERO_KEY = 0
    }
}