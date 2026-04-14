package ru.tbank.petcare.domain.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.model.PetInfo
import ru.tbank.petcare.domain.model.Tip
import ru.tbank.petcare.domain.model.ValidationResult

interface PetsRepository {

    fun getCurrentUsersPets(): Flow<List<Pet>>

    suspend fun syncCurrentUsersPets(): ValidationResult<Unit>
    suspend fun addPet(pet: Pet): ValidationResult<Pet>
    suspend fun editPet(pet: Pet): ValidationResult<Unit>
    suspend fun deletePet(petId: String): ValidationResult<Unit>
    suspend fun updatePetHighScore(petId: String, newScore: Int): ValidationResult<Unit>
    fun getLocalPetById(petId: String): Flow<Pet>
    fun getRemotePetById(petId: String): Flow<Pet>
    fun getAllPublicPets(): Flow<List<Pet>>
    fun getAllTips(): Flow<List<Tip>>

    suspend fun getPetInfo(breed: String): ValidationResult<PetInfo>
    suspend fun uploadPetPhoto(uri: Uri): ValidationResult<String>
    suspend fun deleteAllCurrentUsersPets(): ValidationResult<Unit>
}
