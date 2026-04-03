package ru.tbank.petcare.presentation.screen.registration

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.usecase.RegisterUseCase
import ru.tbank.petcare.domain.usecase.SignInWithGoogleUseCase
import ru.tbank.petcare.utils.AuthFieldsValidator
import ru.tbank.petcare.utils.ErrorParser
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val errorParser: ErrorParser,
    private val authFieldsValidator: AuthFieldsValidator
) : ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    fun processCommand(command: RegistrationCommand) {
        when (command) {
            is RegistrationCommand.InputEmail -> {
                _state.update { state ->
                    state.copy(
                        email = command.email,
                        emailError = ""
                    )
                }
            }

            is RegistrationCommand.InputPassword -> {
                _state.update { state ->
                    state.copy(
                        password = command.password,
                        passwordError = ""
                    )
                }
            }

            is RegistrationCommand.InputRepeatPassword -> {
                _state.update { state ->
                    state.copy(
                        repeatPassword = command.repeatPassword,
                        repeatPasswordError = ""
                    )
                }
            }

            is RegistrationCommand.ChangePasswordVisibility -> {
                _state.update { state ->
                    state.copy(isPasswordVisibility = !state.isPasswordVisibility)
                }
            }

            is RegistrationCommand.ChangeRepeatPasswordVisibility -> {
                _state.update { state ->
                    state.copy(isRepeatPasswordVisibility = !state.isRepeatPasswordVisibility)
                }
            }

            RegistrationCommand.RegisterUserFromEmailAndPassword -> registerWithEmail()
            is RegistrationCommand.SignInWithGoogle -> signInWithGoogle(command.context)
        }
    }

    private fun registerWithEmail() {
        val currentState = _state.value

        val emailError = authFieldsValidator.validateEmail(currentState.email)
        val passwordError = authFieldsValidator.validatePassword(currentState.password)
        val repeatPasswordError = authFieldsValidator.validateRepeatPassword(
            password = currentState.password,
            repeatPassword = currentState.repeatPassword
        )

        if (emailError != null || passwordError != null || repeatPasswordError != null) {
             _state.update {
                 it.copy(
                     emailError = emailError?.let { e -> errorParser.getErrorMessage(e) } ?: "",
                     passwordError = passwordError?.let { e -> errorParser.getErrorMessage(e) } ?: "",
                     repeatPasswordError = repeatPasswordError?.let { e -> errorParser.getErrorMessage(e) } ?: "",
                 )
             }
             return
        }

        _state.update {
            it.copy(
                isLoading = true,
                error = "",
                emailError = "",
                passwordError = "",
                repeatPasswordError = ""
            )
        }

        viewModelScope.launch {
            val result = registerUseCase(
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

    private fun signInWithGoogle(context: Context) {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = signInWithGoogleUseCase(context = context)

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
}

sealed interface RegistrationCommand {
    data class InputEmail(val email: String) : RegistrationCommand
    data class InputPassword(val password: String) : RegistrationCommand
    data class InputRepeatPassword(val repeatPassword: String) : RegistrationCommand
    data class ChangePasswordVisibility(val isVisible: Boolean) : RegistrationCommand
    data class ChangeRepeatPasswordVisibility(val isVisible: Boolean) : RegistrationCommand
    data object RegisterUserFromEmailAndPassword : RegistrationCommand
    data class SignInWithGoogle(val context: Context) : RegistrationCommand
}
