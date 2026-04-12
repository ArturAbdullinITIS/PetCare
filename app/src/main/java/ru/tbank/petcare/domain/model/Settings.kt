package ru.tbank.petcare.domain.model

data class Settings(
    val language: Language = Language.ENGLISH,
    val darkTheme: Boolean = false,
    val notifications: NotificationSettings = NotificationSettings()
)

data class NotificationSettings(
    val enabled: Boolean = false,
    val walk: Boolean = false,
    val grooming: Boolean = false,
    val vet: Boolean = false
)

enum class Language(val tag: String) {
    ENGLISH("en"), RUSSIAN("ru")
}
