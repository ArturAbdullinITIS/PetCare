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
import ru.tbank.petcare.presentation.screen.login.LoginCommand
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
): ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    fun processCommand(command: RegistrationCommand) {
        when (command) {
            is RegistrationCommand.InputEmail -> {
                _state.update { state -> state.copy(email = command.email) }
            }
            is RegistrationCommand.InputPassword -> {
                _state.update { state -> state.copy(password = command.password) }
            }
            is RegistrationCommand.ChangePasswordVisibility -> {
                _state.update { state -> state.copy(isPasswordVisibility = !state.isPasswordVisibility) }
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
        if (currentState.email.isBlank() || currentState.password.isBlank()) return

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
    data class ChangePasswordVisibility(val isVisible: Boolean): RegistrationCommand
    data object RegisterUserFromEmailAndPassword: RegistrationCommand
    data class SignInWithGoogle(val context: Context): RegistrationCommand
}

data class RegistrationState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisibility: Boolean = false,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val error: String = ""
)




