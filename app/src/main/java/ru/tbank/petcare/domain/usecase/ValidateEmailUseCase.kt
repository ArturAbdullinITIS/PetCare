package ru.tbank.petcare.domain.usecase

import ru.tbank.petcare.domain.model.ValidationError
import ru.tbank.petcare.domain.model.ValidationResult
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor() {
    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(isSuccess = false, error = ValidationError.EMAIL_BLANK)
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(isSuccess = false, error = ValidationError.EMAIL_INVALID)
        }
        return ValidationResult(isSuccess = true)
    }
}

