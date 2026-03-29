package com.miru.sdk.core.ext

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

/**
 * Converts an Instant to a human-readable local date-time string.
 *
 * Uses the system default timezone.
 *
 * @return a formatted string representation of the instant
 */
fun Instant.toLocalString(): String {
    return try {
        val dateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
        "${dateTime.year}-${dateTime.monthNumber.toString().padStart(2, '0')}-" +
                "${dateTime.dayOfMonth.toString().padStart(2, '0')} " +
                "${dateTime.hour.toString().padStart(2, '0')}:" +
                "${dateTime.minute.toString().padStart(2, '0')}"
    } catch (e: Exception) {
        this.toString()
    }
}

/**
 * Returns a human-readable string describing how long ago this instant was.
 *
 * @return a string like "2 hours ago", "just now", etc.
 */
fun Instant.timeAgo(): String {
    val now = Clock.System.now()
    val diffMillis = (now - this).inWholeMilliseconds

    return when {
        diffMillis < 1000 -> "just now"
        diffMillis < 60000 -> "${diffMillis / 1000} seconds ago"
        diffMillis < 3600000 -> "${diffMillis / 60000} minutes ago"
        diffMillis < 86400000 -> "${diffMillis / 3600000} hours ago"
        diffMillis < 604800000 -> "${diffMillis / 86400000} days ago"
        diffMillis < 2592000000 -> "${diffMillis / 604800000} weeks ago"
        else -> "${diffMillis / 2592000000} months ago"
    }
}

/**
 * Formats a LocalDate to a string representation.
 *
 * @return a formatted string in "YYYY-MM-DD" format
 */
fun LocalDate.formatAsString(): String {
    return "$year-${monthNumber.toString().padStart(2, '0')}-" +
            "${dayOfMonth.toString().padStart(2, '0')}"
}

/**
 * Gets today's date in the specified timezone.
 *
 * @param timeZone the timezone to use (defaults to system default)
 * @return today's date
 */
fun Clock.System.todayIn(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate {
    return now().toLocalDateTime(timeZone).date
}
