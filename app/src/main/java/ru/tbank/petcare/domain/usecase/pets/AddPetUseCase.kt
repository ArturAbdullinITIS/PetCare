package ru.tbank.petcare.domain.usecase.pets

import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.PetsRepository
import javax.inject.Inject

class AddPetUseCase @Inject constructor(
    private val petsRepository: PetsRepository
) {
    suspend operator fun invoke(pet: Pet): ValidationResult<Pet> {
        return petsRepository.addPet(pet)
    }
}
