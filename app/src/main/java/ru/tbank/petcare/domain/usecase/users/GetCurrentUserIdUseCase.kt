package ru.tbank.petcare.domain.usecase.users

import kotlinx.coroutines.flow.Flow
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Flow<ValidationResult<String>> {
        return authRepository.getCurrentUserId()
    }
}
