package ru.tbank.petcare.domain.usecase.pets

import kotlinx.coroutines.flow.Flow
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.repository.PetsRepository
import javax.inject.Inject

class GetRemotePetUseCase @Inject constructor(
    private val petsRepository: PetsRepository
) {
    operator fun invoke(petId: String): Flow<Pet> {
        return petsRepository.getRemotePetById(petId)
    }
}
