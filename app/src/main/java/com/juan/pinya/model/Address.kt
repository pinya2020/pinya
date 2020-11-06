package com.juan.pinya.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(

    @SerializedName("addressId")
    val addressId: String = "",

    @SerializedName(ADDRESS_KET)
    val address: String = "",

    @SerializedName("price")
    val cost: Int = Int.MIN_VALUE
) : Parcelable {

    companion object {
        const val DIR_NAME = "地點"
        const val ADDRESS_KET = "address"
    }
}