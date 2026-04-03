package ru.tbank.petcare.domain.usecase

import android.util.Patterns.EMAIL_ADDRESS
import ru.tbank.petcare.domain.model.ErrorType
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): ValidationResult<Unit> {
        return authRepository.signInWithEmailAndPassword(email, password)
    }
}
