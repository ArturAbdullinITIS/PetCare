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

        val NOTIF_ENABLED = booleanPreferencesKey("notif_enabled")
        val NOTIF_WALK = booleanPreferencesKey("notif_walk")
        val NOTIF_GROOMING = booleanPreferencesKey("notif_grooming")
        val NOTIF_VET = booleanPreferencesKey("notif_vet")
    }

    override fun getSettings(): Flow<Settings> {
        return dataStore.data.map { prefs ->
            val language = runCatching {
                Language.valueOf(prefs[Keys.LANGUAGE] ?: Language.RUSSIAN.name)
            }.getOrDefault(Language.RUSSIAN)
            Settings(
                language = language,
                darkTheme = prefs[Keys.DARK_THEME] ?: false,
                notifications = NotificationSettings(
                    enabled = prefs[NOTIF_ENABLED] ?: true,
                    walk = prefs[NOTIF_WALK] ?: true,
                    grooming = prefs[NOTIF_GROOMING] ?: true,
                    vet = prefs[NOTIF_VET] ?: true
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

    override suspend fun updateNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[NOTIF_ENABLED] = enabled
        }
    }

    override suspend fun updateWalkNotifications(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[NOTIF_WALK] = enabled
        }
    }

    override suspend fun updateGroomingNotifications(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[NOTIF_GROOMING] = enabled
        }
    }

    override suspend fun updateVetNotifications(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[NOTIF_VET] = enabled
        }
    }
}
