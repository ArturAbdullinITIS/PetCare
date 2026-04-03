package ru.tbank.petcare.presentation.screen.addpet

sealed interface AddPetEvent {
    object Saved : AddPetEvent
    data class Error(val message: String) : AddPetEvent
}
