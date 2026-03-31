package ru.tbank.petcare.presentation.screen.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.usecase.LoginUseCase
import ru.tbank.petcare.domain.usecase.SignInWithGoogleUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
): ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun processCommand(command: LoginCommand) {
        when (command) {
            is LoginCommand.ChangePasswordVisibility -> {
                _state.update {state ->
                    state.copy(isPasswordVisibility = !state.isPasswordVisibility)
                }
            }
            is LoginCommand.InputEmail -> {
                _state.update { state ->
                    state.copy(email = command.email)
                }
            }
            is LoginCommand.InputPassword -> {
                _state.update { state ->
                    state.copy(password = command.password)
                }
            }
            LoginCommand.LoginUserFromEmailAndPassword -> loginWithEmail()

            is LoginCommand.SignInWithGoogle -> signInWithGoogle(command.context)
        }
    }

    private fun signInWithGoogle(context: android.content.Context) {
        _state.update { state -> state.copy(isLoading = true) }
        viewModelScope.launch {
            signInWithGoogleUseCase(context).onSuccess {
                _state.update { state -> state.copy(isSuccess = true, error = "", isLoading = false) }
            }.onFailure { error ->
                _state.update { state -> state.copy(isSuccess = false, error = error.message ?: "Unknown error", isLoading = false) }
            }
        }
    }

    private fun loginWithEmail() {
        val currentState = _state.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) return

        _state.update { state -> state.copy(isLoading = true) }

        viewModelScope.launch {
            loginUseCase(
                email = currentState.email,
                password = currentState.password
            ).onSuccess {
                _state.update { state -> state.copy(isSuccess = true, error = "", isLoading = false) }
            }.onFailure { error ->
                _state.update { state -> state.copy(isSuccess = false, error = error.message ?: "Unknown error", isLoading = false) }
            }
        }

    }
}

sealed interface LoginCommand {
    data class InputEmail(val email: String): LoginCommand
    data class InputPassword(val password: String): LoginCommand
    data class ChangePasswordVisibility(val isVisible: Boolean): LoginCommand
    data object LoginUserFromEmailAndPassword: LoginCommand
    data class SignInWithGoogle(val context: Context): LoginCommand
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisibility: Boolean = false,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val error: String = ""
)