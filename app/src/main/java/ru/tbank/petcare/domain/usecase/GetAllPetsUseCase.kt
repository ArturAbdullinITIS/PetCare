package ru.tbank.petcare.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.repository.PetsRepository
import javax.inject.Inject

class GetAllPetsUseCase @Inject constructor(
    private val petsRepository: PetsRepository
) {
    operator fun invoke(): Flow<List<Pet>> {
        return petsRepository.getCurrentUsersPets()
    }
}
