package ru.tbank.petcare.presentation.model

import ru.tbank.petcare.domain.model.ActivityType
import ru.tbank.petcare.domain.model.IconStatus
import java.util.Date

data class PetCardUIModel(
    val id: String = "",
    val name: String = "",
    val photoUrl: String = "",
    val iconStatus: IconStatus = IconStatus.NONE,
    val subtitle: String = "",
    val lastActivityType: ActivityType? = null,
    val lastActivityDate: Date? = null,
    val isBirthdayToday: Boolean = false
)
