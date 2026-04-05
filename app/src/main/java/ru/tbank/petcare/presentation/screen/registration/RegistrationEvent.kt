package ru.tbank.petcare.presentation.screen.registration

sealed interface RegistrationEvent {
    data object Success : RegistrationEvent
    data class Error(val message: String) : RegistrationEvent
}
