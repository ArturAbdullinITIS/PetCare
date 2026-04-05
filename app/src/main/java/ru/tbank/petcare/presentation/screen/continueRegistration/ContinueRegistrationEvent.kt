package ru.tbank.petcare.presentation.screen.continueRegistration

interface ContinueRegistrationEvent {
    object Saved : ContinueRegistrationEvent
    data class Error(val message: String) : ContinueRegistrationEvent
}
