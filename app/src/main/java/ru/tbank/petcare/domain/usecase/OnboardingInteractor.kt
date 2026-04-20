package ru.tbank.petcare.domain.usecase

import kotlinx.coroutines.flow.first
import ru.tbank.petcare.data.local.OnBoardingPreferencesDataSource
import javax.inject.Inject

class OnboardingInteractor @Inject constructor(
    private val onboardingPreferencesDataSource: OnBoardingPreferencesDataSource
) {
    suspend fun setOnBoardingShown(value: Boolean) {
        onboardingPreferencesDataSource.setOnBoardingShown(value)
    }
    suspend fun getOnBoardingShown(): Boolean {
        return onboardingPreferencesDataSource.isOnBoardingShown.first()
    }
}
