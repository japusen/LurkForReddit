package com.example.lurkforreddit.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.lurkforreddit.R
import kotlinx.datetime.DateTimePeriod

@Composable
fun TimeStamp(
    relativeTime: DateTimePeriod,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val resources = context.resources

    if (relativeTime.years > 0) {
        Text(
            text = resources.getQuantityString(R.plurals.timeStampYears, relativeTime.years, relativeTime.years)
        )
    } else if (relativeTime.months > 0) {
        Text(
            text = resources.getQuantityString(R.plurals.timeStampMonths, relativeTime.months, relativeTime.months)
        )
    } else if (relativeTime.days > 0) {
        Text(
            text = resources.getQuantityString(R.plurals.timeStampDays, relativeTime.days, relativeTime.days)
        )
    } else if (relativeTime.hours > 0) {
        Text(
            text = resources.getQuantityString(R.plurals.timeStampHours, relativeTime.hours, relativeTime.hours)
        )
    } else if (relativeTime.minutes > 0) {
        Text(
            text = resources.getQuantityString(R.plurals.timeStampMinutes, relativeTime.minutes, relativeTime.minutes)
        )
    } else {
        Text(
            text = resources.getQuantityString(R.plurals.timeStampSeconds, relativeTime.seconds, relativeTime.minutes)
        )
    }
}