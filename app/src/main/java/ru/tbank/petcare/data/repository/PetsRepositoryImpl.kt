package ru.tbank.petcare.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.tbank.petcare.data.mapper.toDomain
import ru.tbank.petcare.data.mapper.toDto
import ru.tbank.petcare.data.remote.firebase.PetDto
import ru.tbank.petcare.data.remote.firebase.TipDto
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.model.Tip
import ru.tbank.petcare.domain.repository.PetsRepository
import java.io.IOException
import javax.inject.Inject
import kotlin.jvm.java

class PetsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : PetsRepository {
    companion object {
        private const val OWNER_ID_KEY = "owner_id"
        private const val COLLECTION_PATH = "pets"
        private const val IS_PUBLIC_KEY = "is_public"
        private const val TIPS_KEY = "tips"
    }
    private val collection = firestore.collection(COLLECTION_PATH)

    override fun getCurrentUsersPets(): Flow<List<Pet>> = callbackFlow {
        val currentUserId = firebaseAuth.currentUser?.uid
        if (currentUserId == null) {
            trySend(emptyList())
            close(IllegalStateException("Not Authenticated"))
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
    }.flowOn(Dispatchers.IO)
        .catch { e ->
            emit(emptyList())
        }

    override suspend fun addPet(pet: Pet): Result<Pet> = withContext(Dispatchers.IO) {
        try {
            val currentUserId = firebaseAuth.currentUser?.uid
                ?: return@withContext Result.failure(IllegalStateException("Not Authenticated"))

            val petToSave = pet.copy(id = "", ownerId = currentUserId)
            val dto = petToSave.toDto()

            val docRef = collection.add(dto).await()
            val savedPet = dto.copy(id = docRef.id)

            Result.success(savedPet.toDomain())
        } catch (e: FirebaseFirestoreException) {
            when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED ->
                    Result.failure(SecurityException("Not authorized to add pet"))

                FirebaseFirestoreException.Code.UNAVAILABLE ->
                    Result.failure(IOException("Network unavailable"))

                else -> Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun editPet(pet: Pet): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (pet.id.isBlank()) {
                return@withContext Result.failure(IllegalArgumentException("Pet ID cannot be blank"))
            }

            val currentUserId = firebaseAuth.currentUser?.uid
                ?: return@withContext Result.failure(IllegalStateException("Not Authenticated"))

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
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePet(petId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (petId.isBlank()) {
                return@withContext Result.failure(IllegalArgumentException("Pet ID cannot be blank"))
            }
            val currentUserId = firebaseAuth.currentUser?.uid
            if (currentUserId == null) {
                return@withContext Result.failure(IllegalStateException("Not Authenticated"))
            }

            collection.document(petId).delete().await()
            Result.success(Unit)
        } catch (e: FirebaseFirestoreException) {
            when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
                    Result.failure(SecurityException("Not authorized to delete pet"))
                }

                FirebaseFirestoreException.Code.UNAVAILABLE -> {
                    Result.failure(IOException("Network unavailable"))
                }

                else -> {
                    Result.failure(e)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getPetById(petId: String): Flow<Pet> = callbackFlow {
        if (petId.isBlank()) {
            close(IllegalArgumentException("Pet ID cannot be blank"))
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
                    close(IllegalStateException("Pet not found"))
                }
            }
        awaitClose {
            listener.remove()
        }
    }.flowOn(Dispatchers.IO)
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
    }.flowOn(Dispatchers.IO)
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
    }.flowOn(Dispatchers.IO)
}
