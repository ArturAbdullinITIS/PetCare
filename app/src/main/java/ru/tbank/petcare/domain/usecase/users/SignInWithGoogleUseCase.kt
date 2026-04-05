package ru.tbank.petcare.domain.usecase.users

import android.content.Context
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(context: Context): ValidationResult<Unit> {
        return authRepository.signInWithGoogle(context)
    }
}
