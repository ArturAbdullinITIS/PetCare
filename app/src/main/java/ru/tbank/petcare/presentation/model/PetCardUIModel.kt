package ru.tbank.petcare.presentation.model

import ru.tbank.petcare.domain.model.IconStatus

data class PetCardUIModel(
    val id: String,
    val name: String,
    val photoUrl: String,
    val iconStatus: IconStatus,
    val subtitle: String
)
