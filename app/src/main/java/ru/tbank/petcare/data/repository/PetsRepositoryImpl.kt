package ru.tbank.petcare.data.repository

import android.R.attr.data
import android.R.attr.name
import android.util.Log.e
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.tbank.petcare.data.mapper.toDomain
import ru.tbank.petcare.data.mapper.toDto
import ru.tbank.petcare.data.mapper.toEntities
import ru.tbank.petcare.data.remote.firebase.PetDto
import ru.tbank.petcare.data.remote.firebase.TipDto
import ru.tbank.petcare.data.remote.network.AnimalsApiService
import ru.tbank.petcare.di.IoDispatcher
import ru.tbank.petcare.domain.model.ErrorType
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.model.PetInfo
import ru.tbank.petcare.domain.model.Tip
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.PetsRepository
import javax.inject.Inject
import kotlin.jvm.java

class PetsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    @IoDispatcher private val dispatcherIO: CoroutineDispatcher,
    private val animalsApiService: AnimalsApiService
) : PetsRepository {
    companion object {
        private const val OWNER_ID_KEY = "owner_id"
        private const val COLLECTION_PATH = "pets"
        private const val IS_PUBLIC_KEY = "is_public"
        private const val TIPS_KEY = "tips"

        private const val PET_ID_ERROR = "Pet ID cannot be blank"
        private const val NOT_AUTHENTICATED_ERROR = "Not Authenticated"
        private const val PET_NOT_FOUND_ERROR = "Pet not found"
    }
    private val collection = firestore.collection(COLLECTION_PATH)

    override fun getCurrentUsersPets(): Flow<List<Pet>> = callbackFlow {
        val currentUserId = firebaseAuth.currentUser?.uid
        if (currentUserId == null) {
            trySend(emptyList())
            close(IllegalStateException(NOT_AUTHENTICATED_ERROR))
            return@callbackFlow
        }

        val listener = collection
            .whereEqualTo(OWNER_ID_KEY, currentUserId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val pets = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(PetDto::class.java)?.toDomain()
                } ?: emptyList()

                trySend(pets)
            }

        awaitClose { listener.remove() }
    }.flowOn(dispatcherIO)
        .catch { e ->
            emit(emptyList())
        }

    override suspend fun addPet(pet: Pet): ValidationResult<Pet> = withContext(dispatcherIO) {
        try {
            val currentUserId = firebaseAuth.currentUser?.uid
                ?: return@withContext ValidationResult(
                    error = ErrorType.AuthenticationError(
                        NOT_AUTHENTICATED_ERROR
                    )
                )

            val petToSave = pet.copy(id = "", ownerId = currentUserId)
            val dto = petToSave.toDto()

            val docRef = collection.add(dto).await()
            val savedPet = dto.copy(id = docRef.id)

            ValidationResult(
                data = savedPet.toDomain(),
                isSuccess = true
            )
        } catch (e: FirebaseFirestoreException) {
            when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED ->
                    ValidationResult(
                        error = ErrorType.NetworkError(e.message ?: "")
                    )

                FirebaseFirestoreException.Code.UNAVAILABLE ->
                    ValidationResult(
                        error = ErrorType.NetworkError(e.message ?: "")
                    )

                else -> ValidationResult(
                    error = ErrorType.NetworkError(e.message ?: "")
                )
            }
        } catch (e: Exception) {
            ValidationResult(
                error = ErrorType.CommonError(e.message ?: "")
            )
        }
    }

    override suspend fun editPet(pet: Pet): ValidationResult<Unit> = withContext(dispatcherIO) {
        try {
            if (pet.id.isBlank()) {
                return@withContext ValidationResult(
                    error = ErrorType.CommonError(PET_ID_ERROR)
                )
            }

            val currentUserId = firebaseAuth.currentUser?.uid
                ?: return@withContext ValidationResult(
                    error = ErrorType.AuthenticationError(NOT_AUTHENTICATED_ERROR)
                )

            val updates = mapOf(
                "name" to pet.name,
                "breed" to pet.breed,
                "weight" to pet.weight,
                "date_of_birth" to pet.dateOfBirth,
                "gender" to pet.gender.name,
                "icon_status" to pet.iconStatus.name,
                "is_public" to pet.isPublic,
                "note" to pet.note,
                "photo_url" to pet.photoUrl,
                "game_score" to pet.gameScore,
                "owner_id" to currentUserId
            )

            collection.document(pet.id).set(updates, SetOptions.merge()).await()
            ValidationResult(
                isSuccess = true
            )
        } catch (e: Exception) {
            ValidationResult(
                error = ErrorType.CommonError(e.message ?: "")
            )
        }
    }

    override suspend fun deletePet(petId: String): ValidationResult<Unit> = withContext(dispatcherIO) {
        try {
            if (petId.isBlank()) {
                return@withContext ValidationResult(
                    error = ErrorType.CommonError(NOT_AUTHENTICATED_ERROR)
                )
            }
            val currentUserId = firebaseAuth.currentUser?.uid
            if (currentUserId == null) {
                return@withContext ValidationResult(
                    error = ErrorType.AuthenticationError(NOT_AUTHENTICATED_ERROR)
                )
            }

            collection.document(petId).delete().await()
            ValidationResult(
                isSuccess = true
            )
        } catch (e: FirebaseFirestoreException) {
            when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
                    ValidationResult(
                        error = ErrorType.NetworkError(e.message ?: "")
                    )
                }

                FirebaseFirestoreException.Code.UNAVAILABLE -> {
                    ValidationResult(
                        error = ErrorType.NetworkError(e.message ?: "")
                    )
                }

                else -> {
                    ValidationResult(
                        error = ErrorType.NetworkError(e.message ?: "")
                    )
                }
            }
        } catch (e: Exception) {
            ValidationResult(
                error = ErrorType.CommonError(e.message ?: "")
            )
        }
    }

    override fun getPetById(petId: String): Flow<Pet> = callbackFlow {
        if (petId.isBlank()) {
            close(IllegalArgumentException(PET_ID_ERROR))
            return@callbackFlow
        }
        val listener = collection.document(petId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val pet = snapshot?.toObject(PetDto::class.java)?.toDomain()
                if (pet != null) {
                    trySend(pet)
                } else {
                    close()
                }
            }
        awaitClose {
            listener.remove()
        }
    }.flowOn(dispatcherIO)
        .catch { e ->
            emit(Pet())
        }

    override fun getAllPublicPets(): Flow<List<Pet>> = callbackFlow {
        val listener = collection
            .whereEqualTo(IS_PUBLIC_KEY, true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val pets = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(PetDto::class.java)?.toDomain()
                } ?: emptyList()

                trySend(pets)
            }

        awaitClose { listener.remove() }
    }.flowOn(dispatcherIO)
        .catch { e -> emit(emptyList()) }

    override fun getAllTips(): Flow<List<Tip>> = callbackFlow {
        val listener = firestore.collection(TIPS_KEY)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot == null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val tips = snapshot.documents.mapNotNull { doc ->
                    val dto = doc.toObject(TipDto::class.java)
                    if (dto == null) {
                        null
                    } else {
                        dto.toDomain()
                    }
                }

                trySend(tips)
            }
        awaitClose {
            listener.remove()
        }
    }.flowOn(dispatcherIO)

    override suspend fun getPetInfo(breed: String): ValidationResult<PetInfo> = withContext(dispatcherIO) {
        try {
            val animalsResponse = animalsApiService.getAnimalsByBreed(breed).toEntities()
            val animal = animalsResponse.find { it.commonName.equals(breed, ignoreCase = true) }
            return@withContext if (animal != null) {
                ValidationResult(
                    data = animal,
                    isSuccess = true
                )
            } else {
                ValidationResult(
                    error = ErrorType.NotFoundError(PET_NOT_FOUND_ERROR)
                )
            }
        } catch (e: Exception) {
            ValidationResult(
                error = ErrorType.NetworkError(e.message ?: "")
            )
        }
    }
}
