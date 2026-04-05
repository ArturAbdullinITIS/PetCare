package ru.tbank.petcare.presentation.screen.settings

import ru.tbank.petcare.domain.model.Settings

data class SettingsState(
    val settingsConfig: Settings = Settings()
)
