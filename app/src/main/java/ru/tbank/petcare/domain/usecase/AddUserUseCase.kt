package ru.tbank.petcare.domain.usecase

import ru.tbank.petcare.domain.model.User
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.UsersRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(user: User): ValidationResult<Unit> {
        return usersRepository.createUserProfile(user)
    }
}
