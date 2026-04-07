package ru.tbank.petcare.domain.repository

import ru.tbank.petcare.domain.model.Activity
import ru.tbank.petcare.domain.model.ValidationResult

interface ActivityRepository {

    suspend fun createActivity(activity: Activity): ValidationResult<Activity>
}
