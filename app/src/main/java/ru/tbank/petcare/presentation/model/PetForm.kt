package ru.tbank.petcare.presentation.model

import ru.tbank.petcare.domain.model.Gender
import ru.tbank.petcare.domain.model.IconStatus
import java.util.Date

data class PetForm(
    val id: String = "",
    val name: String = "",
    val breed: String = "",
    val gender: Gender = Gender.MALE,
    val isPublic: Boolean = false,
    val note: String = "",
    val weight: String = "",
    val dateOfBirth: Date? = null,
    val dateOfBirthText: String = "",
    val iconStatus: IconStatus = IconStatus.NONE,
    val photoUrl: String = "",
    val gameScore: Int = 0,
    val ownerId: String = "",
    val ownerName: String = "",
    val gameScoreText: String = ""
)
