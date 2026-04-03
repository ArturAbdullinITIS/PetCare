package ru.tbank.petcare.utils

import android.util.Patterns.EMAIL_ADDRESS
import ru.tbank.petcare.domain.model.ErrorType
import javax.inject.Inject

class AuthFieldsValidator @Inject constructor() {

    fun validateEmail(email: String): ErrorType? {
        return when {
            email.isBlank() -> ErrorType.AuthValidation("Email is blank")
            !EMAIL_ADDRESS.matcher(email).matches() -> ErrorType.AuthValidation("Email is invalid")
            else -> null
        }
    }

    fun validatePassword(password: String): ErrorType? {
        return when {
            password.isBlank() -> ErrorType.AuthValidation("Password is blank")
            password.length < MIN_PASSWORD_LENGTH -> ErrorType.AuthValidation(
                "Password is too short (min $MIN_PASSWORD_LENGTH)"
            )
            else -> null
        }
    }

    fun validateRepeatPassword(password: String, repeatPassword: String): ErrorType? {
        return when {
            repeatPassword.isBlank() -> ErrorType.AuthValidation("Repeat password is blank")
            password != repeatPassword -> ErrorType.AuthValidation("Passwords do not match")
            else -> null
        }
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }
}

