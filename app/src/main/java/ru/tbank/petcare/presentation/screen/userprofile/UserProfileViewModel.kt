package ru.tbank.petcare.presentation.screen.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.usecase.pets.GetAllPetsUseCase
import ru.tbank.petcare.domain.usecase.users.GetCurrentUserUseCase
import ru.tbank.petcare.domain.usecase.users.LogoutUseCase
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getAllPetsUseCase: GetAllPetsUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UserProfileState())
    val state = _state.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                _state.update { state ->
                    state.copy(
                        name = listOf(user.firstName, user.lastName).joinToString(" "),
                        email = user.email,
                        avatarUrl = user.photoUrl
                    )
                }
            }
        }
        viewModelScope.launch {
            getAllPetsUseCase().collect { pets ->
                _state.update { state ->
                    state.copy(
                        numberOfPets = pets.size,
                        bestScore = pets.maxBy { it.gameScore }.gameScore
                    )
                }
            }
        }
    }

    fun processCommand(command: UserProfileCommand) {
        when (command) {
            is UserProfileCommand.Logout -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}

sealed interface UserProfileCommand {

    object Logout : UserProfileCommand
}
