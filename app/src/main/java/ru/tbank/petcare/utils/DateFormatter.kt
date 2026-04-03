package ru.tbank.petcare.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
object DateFormatter {

    private const val DAYS_IN_YEAR = 365
    private const val DAYS_IN_MONTH = 30
    private val currentDateFormatter: DateFormat =
        SimpleDateFormat.getDateInstance(DateFormat.SHORT)

    private val dobFormatter: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).apply {
        isLenient = false
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun formatCurrentDate(): String {
        return currentDateFormatter.format(Date())
    }

    fun formatAgeYearsMonths(dateOfBirth: Date?): String {
        if (dateOfBirth == null) return ""

        val nowMs = System.currentTimeMillis()
        val diffMs = (nowMs - dateOfBirth.time).coerceAtLeast(0L)

        val days = TimeUnit.MILLISECONDS.toDays(diffMs)
        val years = days / DAYS_IN_YEAR
        val months = (days % DAYS_IN_YEAR) / DAYS_IN_MONTH

        return "${years}y ${months}m"
    }

    fun formatDob(dateOfBirth: Date?): String {
        if (dateOfBirth == null) return ""
        return dobFormatter.format(dateOfBirth)
    }

    fun parseDobToDateUtc(text: String): Date? {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return null

        return try {
            val parsed = dobFormatter.parse(trimmed)

            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                if (parsed != null) {
                    time = parsed
                }
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            Date(cal.timeInMillis)
        } catch (_: Exception) {
            null
        }
    }

    fun normalizeToStartOfDayUtc(date: Date): Date {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return Date(cal.timeInMillis)
    }
}
