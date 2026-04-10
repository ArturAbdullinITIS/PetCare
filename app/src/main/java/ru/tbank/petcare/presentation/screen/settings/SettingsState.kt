package ru.tbank.petcare.presentation.screen.settings

import ru.tbank.petcare.domain.model.Language
import ru.tbank.petcare.domain.model.Settings

data class SettingsState(
    val settingsConfig: Settings = Settings(),
    val languageState: LanguageState = LanguageState()
)

data class LanguageState(
    val selected: Language = Language.RUSSIAN,
    val ruSelected: Boolean = true,
    val enSelected: Boolean = false
)
