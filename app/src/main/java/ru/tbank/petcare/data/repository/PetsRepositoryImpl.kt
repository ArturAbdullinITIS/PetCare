package ru.tbank.petcare.data.repository

import android.util.Log.e
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.key.Key.Companion.I
import com.google.android.play.integrity.internal.l
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.tbank.petcare.data.mapper.toDomain
import ru.tbank.petcare.data.mapper.toDto
import ru.tbank.petcare.data.remote.firebase.PetDto
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.repository.PetsRepository
import java.io.IOException
import javax.inject.Inject
import kotlin.jvm.java

class PetsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : PetsRepository {
    private val collection = firestore.collection("pets")

    override fun getCurrentUsersPets(): Flow<List<Pet>> = callbackFlow {
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            trySend(emptyList())
            close(IllegalStateException("Not Authenticated"))
            return@callbackFlow
        }

        collection.whereEqualTo("owner_id", currentUser.uid)
            .orderBy("name", Query.Direction.ASCENDING)
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
    }.flowOn(Dispatchers.IO)
        .catch { e ->
            emit(emptyList())
        }

    override suspend fun addPet(pet: Pet): Result<Pet> = withContext(Dispatchers.IO){
        try {
            if (pet.name.isBlank()) {
                return@withContext Result.failure(IllegalStateException("Name cannot be blank"))
            }
            val dto = pet.toDto()

            val docRef = collection.add(dto).await()

            val savedPet = dto.copy(id = docRef.id)

            Result.success(savedPet.toDomain())
        } catch (e: FirebaseFirestoreException) {
            when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
                    Result.failure(SecurityException("Not authorized to add pet"))
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

    override suspend fun editPet(pet: Pet): Result<Unit> = withContext(Dispatchers.IO){
        try {
            if (pet.id.isBlank()) {
                return@withContext Result.failure(IllegalArgumentException("Pet ID cannot be blank"))
            }

            val currentUserId = firebaseAuth.currentUser?.uid

            if (pet.ownerId != currentUserId) {
                return@withContext Result.failure(SecurityException("Not authorized to edit this pet"))
            }

            val dto = pet.toDto()

            collection.document(pet.id).set(dto).await()
            Result.success(Unit)
        } catch (e: FirebaseFirestoreException) {
            when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
                    Result.failure(SecurityException("Not authorized to edit pet"))
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

    override suspend fun deletePet(petId: String): Result<Unit> = withContext(Dispatchers.IO){
        try {
            if(petId.isBlank()) {
                return@withContext Result.failure(IllegalArgumentException("Pet ID cannot be blank"))
            }
            val currentUserId = firebaseAuth.currentUser?.uid
            if(currentUserId == null) {
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

    override fun getPetById(petId: String): Flow<Pet> = callbackFlow{
        if(petId.isBlank()) {
            close(IllegalArgumentException("Pet ID cannot be blank"))
            return@callbackFlow
        }
        collection.document(petId)
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
    }.flowOn(Dispatchers.IO)
        .catch { e ->
            emit(Pet())
        }

    override fun getAllPublicPets(): Flow<List<Pet>> = callbackFlow{
        collection.whereEqualTo("is_public", true)
            .orderBy("name", Query.Direction.ASCENDING)
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
    }.flowOn(Dispatchers.IO)
        .catch {e ->
            emit(emptyList())
        }
}