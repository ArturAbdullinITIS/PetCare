package ru.tbank.petcare.domain.model

data class Settings(
    val language: Language = Language.RUSSIAN,
    val darkTheme: Boolean = false,
    val notifications: NotificationSettings = NotificationSettings()
)

data class NotificationSettings(
    val enabled: Boolean = true,
    val walk: Boolean = true,
    val grooming: Boolean = true,
    val vet: Boolean = true
)

enum class Language {
    ENGLISH, RUSSIAN
}
