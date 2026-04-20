package ru.tbank.petcare.presentation.screen.registration

sealed interface RegistrationEvent {
    data object EmailRegistered : RegistrationEvent
    data object GoogleRegistered : RegistrationEvent
    data class Error(val message: String) : RegistrationEvent
    data object ShowOnboarding : RegistrationEvent
}
