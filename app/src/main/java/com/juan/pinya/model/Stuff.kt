package com.juan.pinya.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Stuff(

    @SerializedName(NAME_KEY)
    val name: String = "",

    @SerializedName(ID_KEY)
    val id: String = "",

    @SerializedName("password")
    val password: String = "",

    @SerializedName("shipping")
    val shipping: Int = Int.MIN_VALUE,

    @SerializedName("carOwner")
    val carOwner: Boolean = false,

    @SerializedName(TAKE_OFFICE_DATE_KEY)
    val takeOfficeDate: Timestamp = Timestamp(Date()),

    @SerializedName(WORK_KEY)
    val work: Boolean = true

) : Parcelable {
    companion object {
        const val DIR_NAME = "員工"
        const val ID_KEY = "id"
        const val ALL_KEY = "0"
        const val NAME_KEY = "name"
        const val WORK_KEY = "work"
        const val TAKE_OFFICE_DATE_KEY = "takeOfficeDate"
    }
}