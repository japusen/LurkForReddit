package com.example.lurkforreddit.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil


/**
 * Convert the epoch time to a relative time
 * @param createdUtc epoch time of a post/comment/etc
 * @return String of relative time in years, months, days, hours, minutes, or seconds
 */
fun relativeTime(createdUtc: Float): String {
    val epochSecondNow = java.time.Instant.now().epochSecond
    val now = Instant.fromEpochSeconds(epochSecondNow)
    val postTime = Instant.fromEpochSeconds(createdUtc.toLong())
    val time = postTime.periodUntil(now, TimeZone.UTC)

    return if (time.years > 0) {
        if (time.years > 1)
            "${time.years} yrs ago"
        else
            "${time.years} yr ago"
    } else if (time.months > 0) {
        if (time.months > 1)
            "${time.months} mos ago"
        else
            "${time.months} mo ago"
    } else if (time.days > 0) {
        if (time.days > 1)
            "${time.days} ds ago"
        else
            "${time.days} d ago"
    } else if (time.hours > 0) {
        if (time.hours > 1)
            "${time.hours} hrs ago"
        else
            "${time.hours} hr ago"
    } else if (time.minutes > 0) {
        if (time.minutes > 1)
            "${time.minutes} mins ago"
        else
            "${time.minutes} min ago"
    } else if (time.seconds > 0)
        "${time.seconds}s ago"
    else
        "just now"
}