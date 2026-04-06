package ru.tbank.petcare.domain.usecase.pets

import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.PetsRepository
import javax.inject.Inject

class DeletePetUseCase @Inject constructor(
    private val petsRepository: PetsRepository
) {
    suspend operator fun invoke(petId: String): ValidationResult<Unit> {
        return petsRepository.deletePet(petId)
    }
}
