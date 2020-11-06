package com.juan.pinya.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Car(
    @SerializedName("id")
    val id: String = "",

    @SerializedName(CAR_ID_KEY)
    val carId: String = "",

    @SerializedName("personInCharge")
    val personInCharge: String = "",

    @SerializedName("loanMonths")
    val loanMonths: Int = Int.MIN_VALUE,

    @SerializedName("loan")
    val loan: Int = Int.MIN_VALUE,

    @SerializedName("loanPayment")
    val loanPayment: Int = Int.MIN_VALUE,

    @SerializedName("paymentDate")
    val paymentDate: Int = Int.MIN_VALUE,

    @SerializedName("violation")
    var violation: Int = Int.MIN_VALUE

) : Parcelable {

    companion object {
        const val DIR_NAME = "車輛"
        const val CAR_ID_KEY = "carId"
    }
}