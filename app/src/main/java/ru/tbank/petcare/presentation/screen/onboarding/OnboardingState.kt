package ru.tbank.petcare.presentation.screen.onboarding

import ru.tbank.petcare.presentation.mapper.toOnboardingPageUIModel
import ru.tbank.petcare.presentation.model.OnboardingPageUIModel

data class OnboardingState(
    val currentPage: Int = 0,
    val totalPages: Int = 3,
    val currentPageUI: OnboardingPageUIModel = toOnboardingPageUIModel(0)
)
