package ru.tbank.petcare.domain.model

data class PetInfo(
    val commonName: String = "",
    val diet: String = "",
    val group: String = "",
    val lifespan: String = "",
    val skinType: String = "",
    val slogan: String = "",
    val weight: String = "",
    val locations: List<String> = emptyList()
)
