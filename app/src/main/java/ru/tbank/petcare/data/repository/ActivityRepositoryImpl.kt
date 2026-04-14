package ru.tbank.petcare.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.tbank.petcare.data.mapper.toDomain
import ru.tbank.petcare.data.mapper.toDto
import ru.tbank.petcare.data.remote.firebase.ActivityDto
import ru.tbank.petcare.di.IoDispatcher
import ru.tbank.petcare.domain.model.Activity
import ru.tbank.petcare.domain.model.ErrorType
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.ActivityRepository
import java.util.Date
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    @IoDispatcher private val dispatcherIO: CoroutineDispatcher
) : ActivityRepository {

    private companion object {
        const val COLLECTION_PATH = "activities"
        const val FIELD_PET_ID = "pet_id"
        const val FIELD_DATE = "date"
    }

    private val collection = firestore.collection(COLLECTION_PATH)

    override suspend fun createActivity(
        petId: String,
        activity: Activity
    ): ValidationResult<Activity> = withContext(dispatcherIO) {
        try {
            val currentUserId = firebaseAuth.currentUser?.uid
            if (currentUserId == null) {
                return@withContext ValidationResult(
                    error = ErrorType.AuthValidation()
                )
            }

            val activityToSave = activity.copy(
                petId = petId,
                ownerId = currentUserId
            )

            val docRef = collection.add(activityToSave.toDto()).await()
            val activityId = docRef.id

            val lastActivityMap = mapOf(
                "id" to activityId,
                "type" to activityToSave.activityType.name,
                "date" to (activityToSave.activityDate?.let { Timestamp(it) })
            )

            firestore.collection("pets")
                .document(petId)
                .update("last_activity", lastActivityMap)
                .await()

            ValidationResult(
                data = activityToSave.copy(id = activityId),
                isSuccess = true
            )
        } catch (e: FirebaseFirestoreException) {
            when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED,
                FirebaseFirestoreException.Code.UNAVAILABLE -> {
                    ValidationResult(
                        error = ErrorType.NetworkError(e.message ?: "")
                    )
                }

                else -> {
                    ValidationResult(
                        error = ErrorType.CommonError(e.message ?: "")
                    )
                }
            }
        }
    }

    override suspend fun getActivitiesByPetId(
        petId: String
    ): ValidationResult<List<Activity>> = withContext(dispatcherIO) {
        try {
            val snapshot = collection
                .whereEqualTo(FIELD_PET_ID, petId)
                .orderBy(FIELD_DATE, Query.Direction.DESCENDING)
                .get()
                .await()

            val activities = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ActivityDto::class.java)?.toDomain()
            }

            ValidationResult(
                data = activities,
                isSuccess = true
            )
        } catch (e: FirebaseFirestoreException) {
            when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED,
                FirebaseFirestoreException.Code.UNAVAILABLE -> {
                    ValidationResult(
                        error = ErrorType.NetworkError(e.message ?: "")
                    )
                }

                else -> {
                    ValidationResult(
                        error = ErrorType.CommonError(e.message ?: "")
                    )
                }
            }
        }
    }

    override suspend fun getLastActivitiesByPetId(
        petId: String,
        limit: Int
    ): ValidationResult<List<Activity>> = withContext(dispatcherIO) {
        try {
            val snapshot = collection
                .whereEqualTo(FIELD_PET_ID, petId)
                .orderBy(FIELD_DATE, Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()

            val activities = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ActivityDto::class.java)?.toDomain()
            }

            ValidationResult(
                data = activities,
                isSuccess = true
            )
        } catch (e: FirebaseFirestoreException) {
            when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED,
                FirebaseFirestoreException.Code.UNAVAILABLE -> {
                    ValidationResult(
                        error = ErrorType.NetworkError(e.message ?: "")
                    )
                }

                else -> {
                    ValidationResult(
                        error = ErrorType.CommonError(e.message ?: "")
                    )
                }
            }
        }
    }

    override suspend fun getActivitiesByPeriod(
        petId: String,
        startDate: Long,
        endDate: Long
    ): ValidationResult<List<Activity>> = withContext(dispatcherIO) {
        try {
            val startTimestamp = Timestamp(Date(startDate))
            val endTimestamp = Timestamp(Date(endDate))

            val snapshot = collection
                .whereEqualTo(FIELD_PET_ID, petId)
                .whereGreaterThanOrEqualTo(FIELD_DATE, startTimestamp)
                .whereLessThanOrEqualTo(FIELD_DATE, endTimestamp)
                .orderBy(FIELD_DATE, Query.Direction.ASCENDING)
                .get()
                .await()

            val activities = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ActivityDto::class.java)?.toDomain()
            }

            ValidationResult(
                data = activities,
                isSuccess = true
            )
        } catch (e: FirebaseFirestoreException) {
            when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED,
                FirebaseFirestoreException.Code.UNAVAILABLE -> {
                    ValidationResult(
                        error = ErrorType.NetworkError(e.message ?: "")
                    )
                }

                else -> {
                    ValidationResult(
                        error = ErrorType.CommonError(e.message ?: "")
                    )
                }
            }
        }
    }
}
