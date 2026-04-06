package ru.tbank.petcare.domain.usecase.users

import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.UsersRepository
import javax.inject.Inject

class GetUserNameUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(userId: String): ValidationResult<String> {
        return usersRepository.getUserNameById(userId)
    }
}
