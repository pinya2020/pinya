package com.juan.pinya.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(

    @SerializedName("addressId")
    val addressId: String = "",

    @SerializedName("address")
    val address: String = "",

    @SerializedName("price")
    val price: Int = Int.MIN_VALUE
) : Parcelable {

    companion object {
        const val DIR_NAME = "地點"
    }
}