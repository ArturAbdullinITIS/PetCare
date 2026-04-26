package ru.tbank.petcare.presentation.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.util.CoilUtils.result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.data.local.OnBoardingPreferencesDataSource
import ru.tbank.petcare.domain.usecase.users.GetCurrentUserIdUseCase
import javax.inject.Inject

sealed class StartDestination {
    data object Onboarding : StartDestination()
    data object Auth : StartDestination()
    data object Main : StartDestination()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val onboardingPreferencesDataSource: OnBoardingPreferencesDataSource
) : ViewModel() {

    private val _startDestination = MutableStateFlow<StartDestination?>(null)
    val startDestination: StateFlow<StartDestination?> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val onboardingShown = onboardingPreferencesDataSource.isOnBoardingShown.first()

            if (!onboardingShown) {
                _startDestination.update {
                    StartDestination.Onboarding
                }
                return@launch
            }

            getCurrentUserIdUseCase().collect { result ->
                _startDestination.update {
                    if (result.isSuccess) {
                        StartDestination.Main
                    } else {
                        StartDestination.Auth
                    }
                }
            }
        }
    }
}
