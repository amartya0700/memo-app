package com.publicprojects.memo.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Utils {
    fun getDateFromTS(date: Long): String {
        if (date == 0L) return ""
        return SimpleDateFormat(
            "dd-MMM-yyyy",
            Locale.getDefault()
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
            if (hour > 12) {
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

    fun isPast(date: Long, startT: String): Boolean {
        if (date == 0L || startT.isBlank()) return false
        var time = date
        startT.split(":").apply {
            time += TimeUnit.MILLISECONDS.convert(first().toLong(), TimeUnit.HOURS) +
                    TimeUnit.MILLISECONDS.convert(last().toLong(), TimeUnit.MINUTES)
        }
        return time < System.currentTimeMillis()
    }
}