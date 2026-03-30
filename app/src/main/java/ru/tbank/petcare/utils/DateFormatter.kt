package ru.tbank.petcare.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

object DateFormater {

    private val formatter = SimpleDateFormat.getDateInstance(DateFormat.SHORT)

    private val dobFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).apply {
        isLenient = false
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun formatCurrentDate(): String {
        return formatter.format(System.currentTimeMillis())
    }

    fun formatAgeYearsMonths(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = (now - timestamp).coerceAtLeast(0L)

        val days = TimeUnit.MILLISECONDS.toDays(diff)
        val years = days / 365
        val months = (days % 365) / 30

        return "${years}y ${months}m"
    }

    fun formatDob(millisUtc: Long): String {
        if (millisUtc == 0L) return ""
        return dobFormatter.format(Date(millisUtc))
    }

    fun parseDobToMillisUtc(text: String): Long? {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return 0L

        return try {
            val date = dobFormatter.parse(trimmed) ?: return null

            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                time = date
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            cal.timeInMillis
        } catch (_: Exception) {
            null
        }
    }

}