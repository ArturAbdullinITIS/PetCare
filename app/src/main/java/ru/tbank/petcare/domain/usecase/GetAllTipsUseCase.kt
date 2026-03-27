package ru.tbank.petcare.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.tbank.petcare.domain.model.Tip
import ru.tbank.petcare.domain.repository.PetsRepository
import javax.inject.Inject

class GetAllTipsUseCase @Inject constructor(
    private val petsRepository: PetsRepository
) {
    operator fun invoke(): Flow<List<Tip>> = petsRepository.getAllTips()
}