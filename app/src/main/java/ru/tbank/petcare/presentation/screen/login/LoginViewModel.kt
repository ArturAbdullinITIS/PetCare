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
import ru.tbank.petcare.utils.AuthFieldsValidator
import ru.tbank.petcare.utils.ErrorParser
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val errorParser: ErrorParser,
    private val authFieldsValidator: AuthFieldsValidator
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun processCommand(command: LoginCommand) {
        when (command) {
            is LoginCommand.ChangePasswordVisibility -> {
                _state.update { state ->
                    state.copy(isPasswordVisibility = !state.isPasswordVisibility)
                }
            }

            is LoginCommand.InputEmail -> {
                _state.update { state ->
                    state.copy(
                        email = command.email,
                        emailError = ""
                    )
                }
            }

            is LoginCommand.InputPassword -> {
                _state.update { state ->
                    state.copy(
                        password = command.password,
                        passwordError = ""
                    )
                }
            }

            LoginCommand.LoginUserFromEmailAndPassword -> loginWithEmail()
            is LoginCommand.SignInWithGoogle -> signInWithGoogle(command.context)
        }
    }

    private fun signInWithGoogle(context: Context) {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = signInWithGoogleUseCase(context)

            if (result.isSuccess) {
                _state.update { it.copy(isSuccess = true, error = "", isLoading = false) }
            } else {
                _state.update {
                    it.copy(
                        isSuccess = false,
                        error = errorParser.getErrorMessage(result.error),
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun loginWithEmail() {
        val currentState = _state.value

        val emailError = authFieldsValidator.validateEmail(currentState.email)
        val passwordError = authFieldsValidator.validatePassword(currentState.password)

        if (emailError != null || passwordError != null) {
            _state.update {
                it.copy(
                    emailError = emailError?.let { e -> errorParser.getErrorMessage(e) } ?: "",
                    passwordError = passwordError?.let { e -> errorParser.getErrorMessage(e) } ?: "",
                )
            }
            return
        }

        _state.update {
            it.copy(
                isLoading = true,
                emailError = "",
                passwordError = "",
                error = ""
            )
        }

        viewModelScope.launch {
            val result = loginUseCase(
                email = currentState.email,
                password = currentState.password
            )

            if (result.isSuccess) {
                _state.update { it.copy(isSuccess = true, error = "", isLoading = false) }
            } else {
                val message = errorParser.getErrorMessage(result.error)
                _state.update {
                    it.copy(
                        isSuccess = false,
                        isLoading = false,
                        error = message
                    )
                }
            }
        }
    }
}

sealed interface LoginCommand {
    data class InputEmail(val email: String) : LoginCommand
    data class InputPassword(val password: String) : LoginCommand
    data class ChangePasswordVisibility(val isVisible: Boolean) : LoginCommand
    data object LoginUserFromEmailAndPassword : LoginCommand
    data class SignInWithGoogle(val context: Context) : LoginCommand
}
