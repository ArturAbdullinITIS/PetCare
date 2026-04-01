package ru.tbank.petcare.presentation.screen.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.model.ValidationError
import ru.tbank.petcare.domain.usecase.LoginUseCase
import ru.tbank.petcare.domain.usecase.SignInWithGoogleUseCase
import ru.tbank.petcare.domain.usecase.ValidateEmailUseCase
import ru.tbank.petcare.domain.usecase.ValidatePasswordUseCase
import ru.tbank.petcare.utils.ErrorParser
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val errorParser: ErrorParser
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
                    val emailResult = validateEmailUseCase(command.email)
                    state.copy(
                        email = command.email,
                        emailError = errorParser.parse(emailResult.error as ValidationError)
                    )
                }
            }
            is LoginCommand.InputPassword -> {
                _state.update { state ->
                    val passwordResult = validatePasswordUseCase(command.password)
                    state.copy(
                        password = command.password,
                        passwordError = errorParser.parse(passwordResult.error as ValidationError)
                    )
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
        val emailResult = validateEmailUseCase(currentState.email)
        val passwordResult = validatePasswordUseCase(currentState.password)

        val hasError = listOf(
            emailResult,
            passwordResult
        ).any { !it.isSuccess }

        if (hasError) {
             _state.update { state ->
                 state.copy(
                     emailError = errorParser.parse(emailResult.error as ValidationError),
                     passwordError = errorParser.parse(passwordResult.error as ValidationError)
                 )
             }
             return
        }

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
