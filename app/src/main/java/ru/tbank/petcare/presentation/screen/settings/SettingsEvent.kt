package ru.tbank.petcare.presentation.screen.settings

sealed interface SettingsEvent {
    data object ProfileDeleted : SettingsEvent
    data class Error(val message: String) : SettingsEvent
}
