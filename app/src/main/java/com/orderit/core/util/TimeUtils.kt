package com.orderit.core.util

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun timeAgo(isoDate: String): String {
    return try {
        val dateTime = LocalDateTime.parse(isoDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val now = LocalDateTime.now()
        val duration = Duration.between(dateTime, now)

        val minutes = duration.toMinutes()
        val hours = duration.toHours()
        val days = duration.toDays()

        when {
            minutes < 1 -> "Justo ahora"
            minutes < 60 -> "Hace $minutes min"
            hours < 24 -> "Hace $hours h"
            days < 7 -> "Hace $days d"
            else -> "Hace ${days / 7} sem"
        }
    } catch (e: DateTimeParseException) {
        ""
    }
}

fun formatDate(): String {
    val now = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("d 'de' MMMM, yyyy", java.util.Locale("es", "MX"))
    return now.format(formatter)
}
