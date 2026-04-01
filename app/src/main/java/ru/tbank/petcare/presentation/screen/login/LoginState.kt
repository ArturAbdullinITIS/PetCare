package ru.tbank.petcare.presentation.screen.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisibility: Boolean = false,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val error: String = "",
    val emailError: String = "",
    val passwordError: String = ""
)
