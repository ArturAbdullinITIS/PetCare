package ru.tbank.petcare.utils

fun filterFloatInput(raw: String, maxLen: Int = 5): String {
    val normalized = raw.replace(',', '.')

    val filtered = buildString {
        var dotUsed = false
        for (ch in normalized) {
            when {
                ch.isDigit() -> append(ch)
                ch == '.' && !dotUsed -> {
                    if (isEmpty()) continue
                    append('.')
                    dotUsed = true
                }
            }
        }
    }

    return filtered.take(maxLen)
}
