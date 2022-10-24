package com.publicprojects.memo.util

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    const val CONFLICT_EXCEPTION = "com.publicprojects.memo.CONFLICT_EXCEPTION"

    fun getFullDateFromTS(date: Long): String {
        if (date == 0L) return ""
        return SimpleDateFormat(
            "dd-MMM-yyyy", Locale.getDefault()
        ).format(date)
    }

    fun getTimeFromTs(date: Long): String {
        if (date == 0L) return ""
        return SimpleDateFormat(
            "hh:mm aa", Locale.getDefault()
        ).format(date)
    }

    fun getDateFromTs(date: Long): String {
        if (date == 0L) return ""
        return SimpleDateFormat(
            "dd MMMM", Locale.getDefault()
        ).format(date)
    }

    fun getTimeInAmPm(time: String): String {
        if (time.isBlank()) return ""
        var hr: String
        var min: String
        val amPm: String
        time.split(":").apply {
            hr = first()
            min = last()
            var hour = Integer.parseInt(hr)
            if (hour >= 12) {
                amPm = "PM"
                hour -= 12
            } else {
                amPm = "AM"
            }
            hr = if (hour < 10) "0$hour" else "$hour"
            min = if (Integer.parseInt(min) < 10) "0$min" else min
        }
        return "$hr:$min $amPm"
    }

    fun isPast(date: String, startT: String): Boolean {
        if (startT.isBlank()) return false
        return getDateFromDateTime(date, startT)?.let {
                it < Calendar.getInstance().time
            } ?: false
    }

    fun getDateFromDateTime(date: String, time: String): Date? {
        return SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.getDefault()).parse("$date $time")
    }
}