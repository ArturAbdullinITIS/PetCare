package ru.tbank.petcare.domain.usecase

import ru.tbank.petcare.domain.model.ValidationError
import ru.tbank.petcare.domain.model.ValidationResult
import javax.inject.Inject

class ValidateRepeatPasswordUseCase @Inject constructor() {
    operator fun invoke(password: String, repeatPassword: String): ValidationResult {
        if (password != repeatPassword) {
            return ValidationResult(isSuccess = false, error = ValidationError.PASSWORDS_DO_NOT_MATCH)
        }
        return ValidationResult(isSuccess = true)
    }
}

