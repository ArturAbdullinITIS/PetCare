package ru.tbank.petcare.presentation.model

data class PublicPetCardUIModel(
    val id: String,
    val name: String,
    val photoUrl: String,
    val note: String,
    val gameScore: Int,
    val gender: String,
    val breed: String,
)
