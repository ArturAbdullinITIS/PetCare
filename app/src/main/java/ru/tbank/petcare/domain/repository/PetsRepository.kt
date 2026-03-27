package ru.tbank.petcare.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.model.Tip

interface PetsRepository {

    fun getCurrentUsersPets(): Flow<List<Pet>>

    suspend fun addPet(pet: Pet): Result<Pet>
    suspend fun editPet(pet: Pet): Result<Unit>

    suspend fun deletePet(petId: String): Result<Unit>

    fun getPetById(petId: String): Flow<Pet>

    fun getAllPublicPets(): Flow<List<Pet>>

    fun getAllTips(): Flow<List<Tip>>

}