package ru.tbank.petcare.data.repository

import android.net.Uri
import android.util.Log
import coil3.network.HttpException
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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.tbank.petcare.BuildConfig
import ru.tbank.petcare.data.mapper.toDomain
import ru.tbank.petcare.data.mapper.toDto
import ru.tbank.petcare.data.mapper.toEntities
import ru.tbank.petcare.data.remote.firebase.PetDto
import ru.tbank.petcare.data.remote.firebase.TipDto
import ru.tbank.petcare.data.remote.network.animals.AnimalsApiService
import ru.tbank.petcare.data.remote.network.cloudinary.CloudinaryApiService
import ru.tbank.petcare.data.remote.network.cloudinary.ImageBytesProvider
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
    private val animalsApiService: AnimalsApiService,
    private val cloudinaryApiService: CloudinaryApiService,
    private val imageBytesProvider: ImageBytesProvider
) : PetsRepository {
    companion object {
        private const val OWNER_ID_KEY = "owner_id"
        private const val COLLECTION_PATH = "pets"
        private const val IS_PUBLIC_KEY = "is_public"
        private const val TIPS_KEY = "tips"

        private const val PET_ID_ERROR = "Pet ID cannot be blank"
        private const val NOT_AUTHENTICATED_ERROR = "Not Authenticated"
        private const val NO_BREED_INFO = "No breed info"
        private const val NAME = "file"
        private const val FILE_NAME = "pet_photo"
        private const val CONTENT_TYPE = "text/plain"
        private const val CLOUDINARY_ERROR = "Cloudinary error"
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
                    error = ErrorType.FirebaseAuthenticationError(
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
                    error = ErrorType.FirebaseAuthenticationError(NOT_AUTHENTICATED_ERROR)
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
                    error = ErrorType.FirebaseAuthenticationError(NOT_AUTHENTICATED_ERROR)
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
            Log.d("ANIMAL_RESPONSE", animalsResponse.toString())
            val animal = animalsResponse.find { it.commonName.equals(breed, ignoreCase = true) }
            return@withContext if (animal != null) {
                ValidationResult(
                    data = animal,
                    isSuccess = true
                )
            } else {
                ValidationResult(
                    error = ErrorType.CommonError(NO_BREED_INFO)
                )
            }
        } catch (e: Exception) {
            ValidationResult(
                error = ErrorType.NetworkError(e.message ?: "")
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

            val response = cloudinaryApiService.uploadImage(
                cloudName = BuildConfig.CLOUDINARY_CLOUD_NAME,
                file = filePart,
                uploadPreset = presetBody
            )

            ValidationResult(data = response.secureUrl, isSuccess = true)
        } catch (e: HttpException) {
            ValidationResult(
                error = ErrorType.NetworkError(
                    "$CLOUDINARY_ERROR ${e.response.code}: ${e.message}"
                )
            )
        } catch (e: Exception) {
            ValidationResult(error = ErrorType.NetworkError(e.message ?: ""))
        }
    }
}
