package ru.tbank.petcare.presentation.screen.registration

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.model.ValidationError
import ru.tbank.petcare.domain.usecase.RegisterUseCase
import ru.tbank.petcare.domain.usecase.SignInWithGoogleUseCase
import ru.tbank.petcare.domain.usecase.ValidateEmailUseCase
import ru.tbank.petcare.domain.usecase.ValidatePasswordUseCase
import ru.tbank.petcare.domain.usecase.ValidateRepeatPasswordUseCase
import ru.tbank.petcare.utils.ErrorParser
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateRepeatPasswordUseCase: ValidateRepeatPasswordUseCase,
    private val errorParser: ErrorParser
): ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    fun processCommand(command: RegistrationCommand) {
        when (command) {
            is RegistrationCommand.InputEmail -> {
                _state.update { state ->
                    val emailResult = validateEmailUseCase(command.email)
                    state.copy(
                        email = command.email,
                        emailError = emailResult.error?.let { errorParser.parse(it) } ?: ""
                    )
                }
            }
            is RegistrationCommand.InputPassword -> {
                _state.update { state ->
                    val passwordResult = validatePasswordUseCase(command.password)
                    state.copy(
                        password = command.password,
                        passwordError = passwordResult.error?.let { errorParser.parse(it) } ?: ""
                    )
                }
            }
            is RegistrationCommand.InputRepeatPassword -> {
                _state.update { state ->
                    val passwordResult = validateRepeatPasswordUseCase(state.password, command.repeatPassword)
                    state.copy(
                        repeatPassword = command.repeatPassword,
                        repeatPasswordError = passwordResult.error?.let { errorParser.parse(it) } ?: ""
                    )
                }
            }
            is RegistrationCommand.ChangePasswordVisibility -> {
                _state.update { state -> state.copy(isPasswordVisibility = !state.isPasswordVisibility) }
            }
            is RegistrationCommand.ChangeRepeatPasswordVisibility -> {
                _state.update { state -> state.copy(isRepeatPasswordVisibility = !state.isRepeatPasswordVisibility) }
            }
            RegistrationCommand.RegisterUserFromEmailAndPassword -> {
                registerWithEmail()
            }
            is RegistrationCommand.SignInWithGoogle -> {
                signInWithGoogle(command.context)
            }
        }
    }

    private fun registerWithEmail() {
        val currentState = _state.value
        val emailResult = validateEmailUseCase(currentState.email)
        val passwordResult = validatePasswordUseCase(currentState.password)
        val repeatPasswordResult = validateRepeatPasswordUseCase(currentState.password, currentState.repeatPassword)

        val hasError = listOf(
            emailResult,
            passwordResult,
            repeatPasswordResult
        ).any { !it.isSuccess }

        if (hasError) {
             _state.update { state ->
                 state.copy(
                     emailError = emailResult.error?.let { errorParser.parse(it) } ?: "",
                     passwordError = passwordResult.error?.let { errorParser.parse(it) } ?: "",
                     repeatPasswordError = repeatPasswordResult.error?.let { errorParser.parse(it) } ?: ""
                 )
             }
             return
        }

        _state.update { state -> state.copy(isLoading = true) }
        viewModelScope.launch {
            registerUseCase(
                email = currentState.email,
                password = currentState.password
            ).onSuccess {
                _state.update { state -> state.copy(isSuccess = true, error = "", isLoading = false) }
            }.onFailure { error ->
                _state.update { state -> state.copy(isSuccess = false, error = error.message ?: "Unknown error", isLoading = false) }
            }
        }
    }

    private fun signInWithGoogle(context: Context) {
        _state.update { state -> state.copy(isLoading = true) }
        viewModelScope.launch {
            signInWithGoogleUseCase(context = context).onSuccess {
                _state.update { state -> state.copy(isSuccess = true, error = "", isLoading = false) }
            }.onFailure { error ->
                _state.update { state -> state.copy(isSuccess = false, error = error.message ?: "", isLoading = false) }
            }
        }
    }
}

sealed interface RegistrationCommand {
    data class InputEmail(val email: String): RegistrationCommand
    data class InputPassword(val password: String): RegistrationCommand
    data class InputRepeatPassword(val repeatPassword: String): RegistrationCommand
    data class ChangePasswordVisibility(val isVisible: Boolean): RegistrationCommand
    data class ChangeRepeatPasswordVisibility(val isVisible: Boolean): RegistrationCommand
    data object RegisterUserFromEmailAndPassword: RegistrationCommand
    data class SignInWithGoogle(val context: Context): RegistrationCommand
}