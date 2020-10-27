package com.juan.pinya.extention

import java.util.*

fun Calendar.setTodayStartTime(): Calendar {
    return apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

fun Calendar.setMountStarTime(): Calendar{
    return apply {
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}
fun Date.getTomorrowDate(): Date {
    val tomorrowTimeMills = this.time + 24 * 60 * 60 * 1000
    return Date(tomorrowTimeMills)
}