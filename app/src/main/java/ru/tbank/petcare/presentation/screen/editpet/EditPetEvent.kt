package ru.tbank.petcare.presentation.screen.editpet

sealed interface EditPetEvent {
    object Saved : EditPetEvent
    data class Error(val message: String) : EditPetEvent
    object Deleted : EditPetEvent
}
