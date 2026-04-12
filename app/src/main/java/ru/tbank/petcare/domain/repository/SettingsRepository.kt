package ru.tbank.petcare.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.tbank.petcare.domain.model.Language
import ru.tbank.petcare.domain.model.NotificationSettings
import ru.tbank.petcare.domain.model.Settings

interface SettingsRepository {
    fun getSettings(): Flow<Settings>
    suspend fun updateLanguage(language: Language)
    suspend fun updateTheme(darkTheme: Boolean)
    suspend fun updateNotifications(notifications: NotificationSettings)
}
