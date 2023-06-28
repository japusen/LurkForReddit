package com.example.lurkforreddit.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import com.example.lurkforreddit.R
import kotlinx.datetime.DateTimePeriod

@Composable
fun TimeStamp(
    time: DateTimePeriod,
    color: Color,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val resources = context.resources

    if (time.years > 0) {
        Text(
            text = resources.getQuantityString(R.plurals.timeStampYears, time.years, time.years),
            style = style,
            color = color
        )
    } else if (time.months > 0) {
        Text(
            text = resources.getQuantityString(R.plurals.timeStampMonths, time.months, time.months),
            style = style,
            color = color
        )
    } else if (time.days > 0) {
        Text(
            text = resources.getQuantityString(R.plurals.timeStampDays, time.days, time.days),
            style = style,
            color = color
        )
    } else if (time.hours > 0) {
        Text(
            text = resources.getQuantityString(R.plurals.timeStampHours, time.hours, time.hours),
            style = style,
            color = color
        )
    } else if (time.minutes > 0) {
        Text(
            text = resources.getQuantityString(R.plurals.timeStampMinutes, time.minutes, time.minutes),
            style = style,
            color = color
        )
    } else {
        Text(
            text = resources.getQuantityString(R.plurals.timeStampSeconds, time.seconds, time.minutes),
            style = style,
            color = color
        )
    }
}