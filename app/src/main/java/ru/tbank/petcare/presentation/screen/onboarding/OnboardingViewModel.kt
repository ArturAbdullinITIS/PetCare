package ru.tbank.petcare.presentation.screen.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.usecase.OnboardingInteractor
import ru.tbank.petcare.presentation.mapper.toOnboardingPageUIModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingInteractor: OnboardingInteractor
) : ViewModel() {
    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<OnboardingEvents>()
    val events = _events.asSharedFlow()

    fun processCommand(command: OnboardingCommand) {
        when (command) {
            is OnboardingCommand.ChangePage -> {
                val newPage = command.page.coerceIn(0, _state.value.totalPages - 1)
                _state.update { state ->
                    state.copy(
                        currentPage = newPage,
                        currentPageUI = toOnboardingPageUIModel(newPage)
                    )
                }
            }
            OnboardingCommand.Finish -> {
                viewModelScope.launch {
                    onboardingInteractor.setOnBoardingShown(true)
                    _events.emit(OnboardingEvents.Finished)
                }
            }

            OnboardingCommand.Reset -> {
                _state.update { state ->
                    state.copy(
                        currentPage = 0,
                        currentPageUI = toOnboardingPageUIModel(0)
                    )
                }
            }
        }
    }
}

sealed interface OnboardingCommand {
    data class ChangePage(val page: Int) : OnboardingCommand
    data object Reset : OnboardingCommand
    data object Finish : OnboardingCommand
}
