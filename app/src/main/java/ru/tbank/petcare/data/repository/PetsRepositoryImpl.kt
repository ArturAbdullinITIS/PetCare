package ru.tbank.petcare.data.repository

import android.net.Uri
import android.util.Log
import coil3.network.HttpException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.tbank.petcare.BuildConfig
import ru.tbank.petcare.R
import ru.tbank.petcare.data.local.PetsDao
import ru.tbank.petcare.data.mapper.toDbModel
import ru.tbank.petcare.data.mapper.toDomain
import ru.tbank.petcare.data.mapper.toDto
import ru.tbank.petcare.data.mapper.toEntities
import ru.tbank.petcare.data.remote.firebase.TipDto
import ru.tbank.petcare.data.remote.network.animals.AnimalsApiService
import ru.tbank.petcare.data.remote.network.cloudinary.CloudinaryApiService
import ru.tbank.petcare.data.remote.network.cloudinary.ImageBytesProvider
import ru.tbank.petcare.di.IoDispatcher
import ru.tbank.petcare.domain.model.ErrorType
import ru.tbank.petcare.domain.model.Gender
import ru.tbank.petcare.domain.model.IconStatus
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.model.PetInfo
import ru.tbank.petcare.domain.model.Tip
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.ConnectivityRepository
import ru.tbank.petcare.domain.repository.PetsRepository
import ru.tbank.petcare.utils.ResourceProvider
import java.util.Date
import javax.inject.Inject
import kotlin.collections.emptyList
import kotlin.jvm.java

@Suppress("LongParameterList", "TooGenericExceptionCaught")
class PetsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    @IoDispatcher private val dispatcherIO: CoroutineDispatcher,
    private val animalsApiService: AnimalsApiService,
    private val cloudinaryApiService: CloudinaryApiService,
    private val imageBytesProvider: ImageBytesProvider,
    private val connectivityRepository: ConnectivityRepository,
    private val petsDao: PetsDao,
    private val resourceProvider: ResourceProvider
) : PetsRepository {
    private companion object {
        const val OWNER_ID_KEY = "owner_id"
        const val COLLECTION_PATH = "pets"
        const val IS_PUBLIC_KEY = "is_public"
        const val TIPS_KEY = "tips"
        const val NAME = "file"
        const val FILE_NAME = "pet_photo"
        const val CONTENT_TYPE = "text/plain"

        const val GAME_SCORE_KEY = "game_score"
    }
    private val collection = firestore.collection(COLLECTION_PATH)

    override fun getCurrentUsersPets(): Flow<List<Pet>> {
        val currentUserId = firebaseAuth.currentUser?.uid ?: return flowOf(emptyList())

        return petsDao.observePetsByOwner(currentUserId)
            .map { dbPets -> dbPets.map { it.toDomain() } }
            .flowOn(dispatcherIO)
    }

    override suspend fun syncCurrentUsersPets(): ValidationResult<Unit> = withContext(dispatcherIO) {
        if (!connectivityRepository.isOnline.value) {
            return@withContext ValidationResult(
                error = ErrorType.NetworkError(
                    resourceProvider.getString(
                        R.string.offline
                    )
                )
            )
        }

        val currentUserId = firebaseAuth.currentUser?.uid
            ?: return@withContext ValidationResult(
                error = ErrorType.FirebaseAuthenticationError(resourceProvider.getString(R.string.not_authenticated))
            )

        return@withContext try {
            val snapshot = collection
                .whereEqualTo(OWNER_ID_KEY, currentUserId)
                .get()
                .await()

            val pets = snapshot.documents.map { it.toPetCompat() }

            petsDao.deleteByOwner(currentUserId)
            petsDao.upsertAll(pets.map { it.toDbModel() })

            ValidationResult(isSuccess = true, data = Unit)
        } catch (e: FirebaseFirestoreException) {
            ValidationResult(
                error = ErrorType.NetworkError(
                    e.message ?: resourceProvider.getString(
                        R.string.sync_error
                    )
                )
            )
        } catch (e: IllegalArgumentException) {
            ValidationResult(
                error = ErrorType.NetworkError(e.message ?: "")
            )
        } catch (e: HttpException) {
            ValidationResult(
                error = ErrorType.NetworkError(e.message ?: "")
            )
        }
    }

    override suspend fun addPet(pet: Pet): ValidationResult<Pet> = withContext(dispatcherIO) {
        try {
            if (!connectivityRepository.isOnline.value) {
                return@withContext ValidationResult(
                    error = ErrorType.NetworkError(
                        resourceProvider.getString(
                            R.string.offline
                        )
                    )
                )
            }

            val currentUserId = firebaseAuth.currentUser?.uid
                ?: return@withContext ValidationResult(
                    error = ErrorType.FirebaseAuthenticationError(
                        resourceProvider.getString(
                            R.string.not_authenticated
                        )
                    )
                )

            val petToSave = pet.copy(id = "", ownerId = currentUserId)
            val dto = petToSave.toDto()

            val docRef = collection.add(dto).await()
            val savedPet = dto.copy(id = docRef.id)

            val domainSaved = savedPet.toDomain()
            petsDao.upsert(domainSaved.toDbModel())

            ValidationResult(data = domainSaved, isSuccess = true)
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
            if (!connectivityRepository.isOnline.value) {
                return@withContext ValidationResult(
                    error = ErrorType.NetworkError(resourceProvider.getString(R.string.offline))
                )
            }

            if (pet.id.isBlank()) {
                return@withContext ValidationResult(
                    error = ErrorType.CommonError(resourceProvider.getString(R.string.pet_id_cannot_be_blank))
                )
            }

            val currentUserId = firebaseAuth.currentUser?.uid
                ?: return@withContext ValidationResult(
                    error = ErrorType.FirebaseAuthenticationError(
                        resourceProvider.getString(
                            R.string.not_authenticated
                        )
                    )
                )

            val updates = mapOf(
                "name" to pet.name,
                "breed" to pet.breed,
                "weight" to pet.weight,
                "date_of_birth" to (pet.dateOfBirth?.let { Timestamp(it) }),
                "gender" to pet.gender.name,
                "icon_status" to pet.iconStatus.name,
                "is_public" to pet.isPublic,
                "note" to pet.note,
                "photo_url" to pet.photoUrl,
                "game_score" to pet.gameScore,
                "owner_id" to currentUserId
            )

            collection.document(pet.id).set(updates, SetOptions.merge()).await()
            petsDao.upsert(pet.copy(ownerId = currentUserId).toDbModel())

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
            if (!connectivityRepository.isOnline.value) {
                return@withContext ValidationResult(
                    error = ErrorType.NetworkError(resourceProvider.getString(R.string.offline))
                )
            }

            if (petId.isBlank()) {
                return@withContext ValidationResult(
                    error = ErrorType.CommonError(resourceProvider.getString(R.string.pet_id_cannot_be_blank))
                )
            }
            val currentUserId = firebaseAuth.currentUser?.uid
            if (currentUserId == null) {
                return@withContext ValidationResult(
                    error = ErrorType.FirebaseAuthenticationError(
                        resourceProvider.getString(R.string.not_authenticated)
                    )
                )
            }

            collection.document(petId).delete().await()
            petsDao.deleteById(petId)

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

    override fun getRemotePetById(petId: String): Flow<Pet> = callbackFlow {
        if (petId.isBlank()) {
            close(IllegalArgumentException(resourceProvider.getString(R.string.pet_id_cannot_be_blank)))
            return@callbackFlow
        }
        val listener = collection.document(petId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val pet = snapshot?.toPetCompat()
                if (pet != null) trySend(pet) else close()
            }
        awaitClose { listener.remove() }
    }.flowOn(dispatcherIO)
        .catch { emit(Pet()) }

    override fun getLocalPetById(petId: String): Flow<Pet> {
        if (petId.isBlank()) return flowOf(Pet())
        return petsDao.observePetById(petId)
            .map { it?.toDomain() ?: Pet() }
            .flowOn(dispatcherIO)
    }

    override fun getAllPublicPets(): Flow<List<Pet>> = callbackFlow {
        val currentUserId = firebaseAuth.currentUser?.uid

        val listener = collection
            .whereEqualTo(IS_PUBLIC_KEY, true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val pets = snapshot?.documents
                    ?.map { it.toPetCompat() }
                    ?.filter { it.ownerId != currentUserId } ?: emptyList()

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
                    dto?.toDomain()
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
            Log.d("ANIMAL_RESPONSE", animalsResponse.toString())
            val animal = animalsResponse.find { it.breedName.equals(breed, ignoreCase = true) }
            return@withContext if (animal != null) {
                ValidationResult(
                    data = animal,
                    isSuccess = true
                )
            } else {
                ValidationResult(
                    error = ErrorType.CommonError(
                        resourceProvider.getString(
                            R.string.no_breed_information_found_for,
                            breed
                        )
                    )
                )
            }
        } catch (e: Exception) {
            ValidationResult(
                error = ErrorType.NetworkError(e.message ?: "")
            )
        }
    }

    override suspend fun updatePetHighScore(petId: String, newScore: Int): ValidationResult<Unit> = withContext(
        dispatcherIO
    ) {
        try {
            val updates = mapOf(
                GAME_SCORE_KEY to newScore
            )
            collection.document(petId).set(updates, SetOptions.merge()).await()
            petsDao.updateGameScore(petId, newScore)
            ValidationResult(isSuccess = true, data = Unit)
        } catch (e: Exception) {
            ValidationResult(
                isSuccess = false,
                error = ErrorType.NetworkError(message = e.message ?: "")
            )
        }
    }

    override suspend fun uploadPetPhoto(uri: Uri): ValidationResult<String> = withContext(dispatcherIO) {
        try {
            val bytes = imageBytesProvider.readBytes(uri)
            val mime = imageBytesProvider.getMimeType(uri)

            val filePart = MultipartBody.Part.createFormData(
                NAME,
                FILE_NAME,
                bytes.toRequestBody(mime.toMediaType())
            )

            val presetBody = BuildConfig.CLOUDINARY_PRESET_NAME
                .toRequestBody(CONTENT_TYPE.toMediaType())

            val folderBody = BuildConfig.CLOUDINARY_PETS_FOLDER
                .toRequestBody(CONTENT_TYPE.toMediaType())

            val response = cloudinaryApiService.uploadImage(
                cloudName = BuildConfig.CLOUDINARY_CLOUD_NAME,
                file = filePart,
                uploadPreset = presetBody,
                folder = folderBody
            )

            ValidationResult(data = response.secureUrl, isSuccess = true)
        } catch (e: HttpException) {
            ValidationResult(
                error = ErrorType.NetworkError(
                    "${e.response.code}: ${e.message}"
                )
            )
        } catch (e: Exception) {
            ValidationResult(error = ErrorType.NetworkError(e.message ?: ""))
        }
    }

    override suspend fun deleteAllCurrentUsersPets(): ValidationResult<Unit> = withContext(dispatcherIO) {
        return@withContext try {
            val currentUserId = firebaseAuth.currentUser?.uid
            if (currentUserId == null) {
                ValidationResult(
                    error = ErrorType.FirebaseAuthenticationError(
                        resourceProvider.getString(R.string.not_authenticated)
                    )
                )
            } else {
                val snapshot = collection.whereEqualTo(OWNER_ID_KEY, currentUserId)
                    .get().await()

                snapshot.documents.chunked(450).forEach { chunk ->
                    val batch = firestore.batch()
                    chunk.forEach { doc -> batch.delete(doc.reference) }
                    batch.commit().await()
                }
                petsDao.deleteByOwner(currentUserId)
                ValidationResult(isSuccess = true)
            }
        } catch (
            e: FirebaseFirestoreException
        ) {
            ValidationResult(
                isSuccess = false,
                error = ErrorType.NetworkError(e.message ?: "")
            )
        }
    }
}

private fun DocumentSnapshot.getDobCompat(): Date? {
    val raw = get("date_of_birth") ?: return null
    return when (raw) {
        is Timestamp -> raw.toDate()
        is Date -> raw
        is Long -> Date(raw)
        is Double -> Date(raw.toLong())
        else -> null
    }
}

private fun DocumentSnapshot.toPetCompat(): Pet {
    return Pet(
        id = id,
        name = getString("name") ?: "",
        breed = getString("breed") ?: "",
        weight = getDouble("weight") ?: 0.0,
        dateOfBirth = getDobCompat(),
        gender = Gender.getGenderFromValue(getString("gender") ?: ""),
        iconStatus = IconStatus.getIconStatusFromValue(getString("icon_status") ?: ""),
        isPublic = getBoolean("is_public") ?: false,
        note = getString("note") ?: "",
        photoUrl = getString("photo_url") ?: "",
        gameScore = (getLong("game_score") ?: 0L).toInt(),
        ownerId = getString("owner_id") ?: ""
    )
}
