package ru.tbank.petcare.domain.usecase.pets

import ru.tbank.petcare.domain.model.Activity
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.ActivityRepository
import javax.inject.Inject

class CreateActivityUseCase @Inject constructor(
    private val activityRepository: ActivityRepository
) {

    suspend operator fun invoke(activity: Activity): ValidationResult<Activity> {
        return activityRepository.createActivity(activity)
    }
}
