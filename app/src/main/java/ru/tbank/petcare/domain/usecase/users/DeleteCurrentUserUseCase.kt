package ru.tbank.petcare.domain.usecase.users

import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.PetsRepository
import ru.tbank.petcare.domain.repository.UsersRepository
import javax.inject.Inject

class DeleteCurrentUserUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
    private val petsRepository: PetsRepository
) {
    suspend operator fun invoke(): ValidationResult<Unit> {
        val petsRes = petsRepository.deleteAllCurrentUsersPets()
        val profileRes = usersRepository.deleteCurrentUser()
        val authRes = usersRepository.deleteCurrentUserAuth()

        return when {
            !petsRes.isSuccess -> petsRes

            !profileRes.isSuccess -> profileRes

            !authRes.isSuccess -> authRes

            else -> ValidationResult(isSuccess = true)
        }
    }
}
