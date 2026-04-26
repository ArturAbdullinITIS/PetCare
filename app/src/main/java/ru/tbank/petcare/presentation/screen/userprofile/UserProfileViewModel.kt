package ru.tbank.petcare.presentation.screen.userprofile

import android.R.attr.name
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.usecase.pets.GetAllPetsUseCase
import ru.tbank.petcare.domain.usecase.users.GetCurrentUserUseCase
import ru.tbank.petcare.domain.usecase.users.LogoutUseCase

@HiltViewModel(assistedFactory = UserProfileViewModel.Factory::class)
class UserProfileViewModel @AssistedInject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getAllPetsUseCase: GetAllPetsUseCase,
    private val logoutUseCase: LogoutUseCase,
    @Assisted(USER_ID) private val userId: String
) : ViewModel() {

    companion object {
        private const val USER_ID = "user_id"
    }
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
                        id = userId,
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
                        bestScore = pets.maxByOrNull { it.gameScore }?.gameScore ?: 0
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

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(USER_ID) userId: String
        ): UserProfileViewModel
    }
}

sealed interface UserProfileCommand {

    object Logout : UserProfileCommand
}
