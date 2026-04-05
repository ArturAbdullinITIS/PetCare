package ru.tbank.petcare.presentation.model

data class UserForm(
    val id: String = "",
    val email: String = "",
    val lastName: String = "",
    val firstName: String = "",
    val photoUrl: String = "",
)
