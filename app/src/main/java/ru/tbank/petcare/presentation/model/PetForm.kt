package ru.tbank.petcare.presentation.model

import ru.tbank.petcare.domain.model.Gender
import ru.tbank.petcare.domain.model.IconStatus

data class PetForm(
    val name: String = "",
    val breed: String = "",
    val gender: Gender = Gender.MALE,
    val isPublic: Boolean = false,
    val note: String = "",
    val weight: String = "",
    val dateOfBirth: Long = 0L,
    val iconStatus: IconStatus = IconStatus.NONE,
    val photoUrl: String = ""
)