package ru.tbank.petcare.presentation.screen.createActivity

sealed interface CreateActivityEvent {
    object Saved : CreateActivityEvent
    data class Error(val message: String) : CreateActivityEvent
}
