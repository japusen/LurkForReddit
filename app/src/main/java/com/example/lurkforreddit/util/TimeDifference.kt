package com.example.lurkforreddit.util

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil

fun relativeTime(createdUtc: Float): DateTimePeriod {
    val epochSecondNow = java.time.Instant.now().epochSecond
    val now = Instant.fromEpochSeconds(epochSecondNow)
    val postTime = Instant.fromEpochSeconds(createdUtc.toLong())

    return postTime.periodUntil(now, TimeZone.UTC)
}