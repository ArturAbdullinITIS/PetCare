package ru.tbank.petcare.domain.usecase.pets

import ru.tbank.petcare.domain.model.PetInfo
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.PetsRepository
import javax.inject.Inject

class GetPetInfoUseCase @Inject constructor(
    private val petsRepository: PetsRepository
) {
    suspend operator fun invoke(breed: String): ValidationResult<PetInfo> {
        return petsRepository.getPetInfo(breed)
    }
}
