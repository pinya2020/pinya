package com.juan.pinya.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Car(

    @SerializedName("carId")
    val carId: String? = null,

    @SerializedName("deadline")
    val deadline: Int? = null,

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("loan")
    val loan: Int? = null,

    @SerializedName("nowDeadline")
    val nowDeadline: Int? = null,

    @SerializedName("payType")
    var payType: Int? = null

) : Parcelable {
    companion object {
        const val DIR_NAME = "車輛"
    }
}