package ru.tbank.petcare.presentation.screen.editprofile

sealed interface EditProfileEvent {
    object Saved : EditProfileEvent
    data class Error(val message: String) : EditProfileEvent
    object LaunchImagePicker : EditProfileEvent
}
