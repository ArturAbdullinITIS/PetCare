package ru.tbank.petcare.domain.usecase.pets

import ru.tbank.petcare.domain.repository.PetsRepository
import javax.inject.Inject

class SyncPetsUseCase @Inject constructor(
    private val petsRepository: PetsRepository
) {
    suspend operator fun invoke() {
        petsRepository.syncCurrentUsersPets()
    }
}
