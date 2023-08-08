package com.example.lurkforreddit.util

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil

/**
 * Convert the epoch time to a relative time
 * @param createdUtc epoch time of a post/comment/etc
 * @return DateTimePeriod to determine time in years, months, days, hours, minutes, or seconds
 */
fun relativeTime(createdUtc: Float): DateTimePeriod {
    val epochSecondNow = java.time.Instant.now().epochSecond
    val now = Instant.fromEpochSeconds(epochSecondNow)
    val postTime = Instant.fromEpochSeconds(createdUtc.toLong())

    return postTime.periodUntil(now, TimeZone.UTC)
}