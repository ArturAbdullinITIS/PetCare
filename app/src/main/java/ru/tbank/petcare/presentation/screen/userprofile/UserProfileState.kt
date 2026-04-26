package ru.tbank.petcare.presentation.screen.userprofile

data class UserProfileState(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val numberOfPets: Int = 0,
    val bestScore: Int = 0,
    val avatarUrl: String? = null
)
