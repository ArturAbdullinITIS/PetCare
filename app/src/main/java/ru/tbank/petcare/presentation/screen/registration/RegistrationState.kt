package ru.tbank.petcare.presentation.screen.registration

data class RegistrationState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val isPasswordVisibility: Boolean = false,
    val isRepeatPasswordVisibility: Boolean = false,
    val isLoading: Boolean = false,
    val error: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val repeatPasswordError: String = ""
)
