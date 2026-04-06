package ru.tbank.petcare.domain.usecase.users

import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): ValidationResult<String> {
        return authRepository.getCurrentUserId()
    }
}
