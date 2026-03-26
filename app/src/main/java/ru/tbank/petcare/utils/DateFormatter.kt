package ru.tbank.petcare.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

object DateFormater {

    private val formatter = SimpleDateFormat.getDateInstance(DateFormat.SHORT)

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
}