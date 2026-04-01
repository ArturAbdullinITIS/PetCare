package ru.tbank.petcare.domain.usecase

import ru.tbank.petcare.domain.model.ValidationError
import ru.tbank.petcare.domain.model.ValidationResult
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
    operator fun invoke(password: String): ValidationResult {
        if (password.length < 6) {
            return ValidationResult(isSuccess = false, error = ValidationError.PASSWORD_SHORT)
        }
        return ValidationResult(isSuccess = true)
    }
}

