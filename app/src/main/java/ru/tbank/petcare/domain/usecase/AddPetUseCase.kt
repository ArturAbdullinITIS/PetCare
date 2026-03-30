package ru.tbank.petcare.domain.usecase

import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.repository.PetsRepository
import ru.tbank.petcare.presentation.model.PetForm
import javax.inject.Inject

class AddPetUseCase @Inject constructor(
    private val petsRepository: PetsRepository
){
    suspend operator fun invoke(pet: Pet): Result<Pet> {

        return petsRepository.addPet(pet)
    }
}