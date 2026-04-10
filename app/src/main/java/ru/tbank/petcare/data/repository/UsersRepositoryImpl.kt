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
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.tbank.petcare.BuildConfig
import ru.tbank.petcare.data.mapper.toDomain
import ru.tbank.petcare.data.mapper.toDto
import ru.tbank.petcare.data.remote.firebase.UserDto
import ru.tbank.petcare.data.remote.network.cloudinary.CloudinaryApiService
import ru.tbank.petcare.data.remote.network.cloudinary.ImageBytesProvider
import ru.tbank.petcare.di.IoDispatcher
import ru.tbank.petcare.domain.model.ErrorType
import ru.tbank.petcare.domain.model.User
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.UsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    @IoDispatcher private val dispatcherIO: CoroutineDispatcher,
    private val imageBytesProvider: ImageBytesProvider,
    private val cloudinaryApiService: CloudinaryApiService
) : UsersRepository {
    companion object {
        private const val COLLECTION_PATH = "users"
        private const val NAME = "file"
        private const val FILE_NAME = "user_photo"
        private const val CONTENT_TYPE = "text/plain"
        private const val CLOUDINARY_ERROR = "Cloudinary error"

        private const val USER_ID_ERROR = "User id cannot be blank"
    }
    private val collection = firestore.collection(COLLECTION_PATH)

    override suspend fun createUserProfile(user: User): ValidationResult<Unit> = withContext(dispatcherIO) {
        try {
            val userId = firebaseAuth.currentUser?.uid ?: return@withContext ValidationResult(
                isSuccess = false,
                error = ErrorType.AuthValidation()
            )
            val userEmail = firebaseAuth.currentUser?.email ?: return@withContext ValidationResult(
                isSuccess = false,
                error = ErrorType.AuthValidation()
            )

            val userToAdd = user.copy(id = userId, email = userEmail).toDto()

            collection
                .document(userId)
                .set(userToAdd, SetOptions.merge())
                .await()

            ValidationResult(isSuccess = true, data = Unit)
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
        }
    }

    override suspend fun uploadUsersPhoto(uri: Uri): ValidationResult<String> = withContext(dispatcherIO) {
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

            val folderBody = BuildConfig.CLOUDINARY_USERS_FOLDER
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
                    "$CLOUDINARY_ERROR ${e.response.code}: ${e.message}"
                )
            )
        }
    }

    override suspend fun getUserNameById(userId: String): ValidationResult<String> = withContext(dispatcherIO) {
        try {
            val userSnapshot = collection.document(userId).get().await()
            val dto = userSnapshot.toObject(UserDto::class.java)
            if (dto == null) {
                return@withContext ValidationResult(
                    isSuccess = false,
                    error = ErrorType.CommonError("User not found")
                )
            }
            val name = listOf(dto.firstName, dto.lastName)
                .filter { it.isNotBlank() }
                .joinToString(" ")
                .ifBlank { dto.email }

            ValidationResult(
                isSuccess = true,
                data = name
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
        }
    }

    override suspend fun getCurrentUser(): Flow<User> = callbackFlow {
        val currentUserId: String = firebaseAuth.currentUser?.uid ?: ""
        Log.d("UserRepositoryImpl", currentUserId)

        if (currentUserId.isBlank()) {
            close(IllegalArgumentException(USER_ID_ERROR))
            return@callbackFlow
        }
        val listener = collection.document(currentUserId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val userDto = snapshot?.toObject(UserDto::class.java)
                val user = userDto?.toDomain()
                Log.d("UserRepositoryImpl", "${user?.email}, ${user?.firstName}")
                if (user != null) {
                    trySend(user)
                } else {
                    close()
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun editUser(user: User): ValidationResult<Unit> = withContext(dispatcherIO) {
        try {
            if (user.id.isBlank()) {
                return@withContext ValidationResult(
                    isSuccess = false,
                    error = ErrorType.CommonError(USER_ID_ERROR)
                )
            }
            val updates = mapOf(
                "first_name" to user.firstName,
                "last_name" to user.lastName,
                "photo_url" to user.photoUrl,
                "email" to user.email
            )

            collection.document(user.id).set(updates, SetOptions.merge()).await()
            ValidationResult(
                isSuccess = true
            )
        } catch (e: FirebaseFirestoreException) {
            ValidationResult(
                error = ErrorType.CommonError(e.message ?: "")
            )
        }
    }
}
