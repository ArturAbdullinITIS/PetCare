package ru.tbank.petcare.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.tbank.petcare.domain.model.Language
import ru.tbank.petcare.domain.model.NotificationSettings
import ru.tbank.petcare.domain.model.Settings
import ru.tbank.petcare.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {
    companion object Keys {
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val LANGUAGE = stringPreferencesKey("language")
        val NOTIF_WALK = booleanPreferencesKey("notif_walk")
        val NOTIF_GROOMING = booleanPreferencesKey("notif_grooming")
        val NOTIF_VET = booleanPreferencesKey("notif_vet")
    }

    override fun getSettings(): Flow<Settings> {
        return dataStore.data.map { prefs ->
            val language = runCatching {
                Language.valueOf(prefs[LANGUAGE] ?: Language.ENGLISH.name)
            }.getOrDefault(Language.ENGLISH)
            val walk = prefs[NOTIF_WALK] ?: false
            val grooming = prefs[NOTIF_GROOMING] ?: false
            val vet = prefs[NOTIF_VET] ?: false
            val enabled = walk || grooming || vet
            Settings(
                language = language,
                darkTheme = prefs[DARK_THEME] ?: false,
                notifications = NotificationSettings(
                    enabled = enabled,
                    walk = walk,
                    grooming = grooming,
                    vet = vet
                )
            )
        }
    }

    override suspend fun updateLanguage(language: Language) {
        dataStore.edit { prefs ->
            prefs[LANGUAGE] = language.name
        }
    }

    override suspend fun updateTheme(darkTheme: Boolean) {
        dataStore.edit { prefs ->
            prefs[DARK_THEME] = darkTheme
        }
    }
    override suspend fun updateNotifications(notifications: NotificationSettings) {
        dataStore.edit { prefs ->
            prefs[NOTIF_WALK] = notifications.walk
            prefs[NOTIF_GROOMING] = notifications.grooming
            prefs[NOTIF_VET] = notifications.vet
        }
    }
}
