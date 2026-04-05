package ru.tbank.petcare.domain.model

data class User(
    val id: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val photoUrl: String = ""
)
