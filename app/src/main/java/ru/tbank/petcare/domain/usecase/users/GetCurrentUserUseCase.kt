package ru.tbank.petcare.domain.usecase.users

import kotlinx.coroutines.flow.Flow
import ru.tbank.petcare.domain.model.User
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.UsersRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {

    suspend operator fun invoke(): Flow<User> {
        return usersRepository.getCurrentUser()
    }
}