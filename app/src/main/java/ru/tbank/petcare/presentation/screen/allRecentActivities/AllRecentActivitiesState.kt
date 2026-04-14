package ru.tbank.petcare.presentation.screen.allRecentActivities

import ru.tbank.petcare.domain.model.Activity

data class AllRecentActivitiesState(
    val activities: List<Activity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
