package ru.tbank.petcare.domain.model

import java.util.Date

data class LastActivity(
    val id: String = "",
    val type: ActivityType = ActivityType.WALK,
    val date: Date? = null
)
