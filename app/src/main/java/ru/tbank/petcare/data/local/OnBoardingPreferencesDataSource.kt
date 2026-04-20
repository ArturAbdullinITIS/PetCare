package ru.tbank.petcare.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OnBoardingPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val ON_BOARDING_KEY = booleanPreferencesKey("onboarding_shown")
    }

    val isOnBoardingShown: Flow<Boolean> =
        dataStore.data.map { prefs ->
            prefs[ON_BOARDING_KEY] ?: false
        }

    suspend fun setOnBoardingShown(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[ON_BOARDING_KEY] = value
        }
    }
}
