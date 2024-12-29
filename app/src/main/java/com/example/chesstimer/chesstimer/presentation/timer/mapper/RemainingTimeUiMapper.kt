package com.example.chesstimer.chesstimer.presentation.timer.mapper

import java.util.Locale
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Long.toDisplayableTime(): String {
    val duration = toDuration(DurationUnit.MILLISECONDS)
    val timeString = duration.toComponents { minutes, seconds, nanoseconds ->
        String.format(Locale.US, "%02d:%02d:%02d", minutes, seconds, nanoseconds / 10000000)
    }

    return timeString
}