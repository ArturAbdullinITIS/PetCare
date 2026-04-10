package ru.tbank.petcare.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.tbank.petcare.data.mapper.toDto
import ru.tbank.petcare.di.IoDispatcher
import ru.tbank.petcare.domain.model.Activity
import ru.tbank.petcare.domain.model.ErrorType
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.ActivityRepository
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    @IoDispatcher private val dispatcherIO: CoroutineDispatcher
) : ActivityRepository {
    companion object {
        private const val COLLECTION_PATH = "activities"
    }

    private val collection = firestore.collection(COLLECTION_PATH)

    override suspend fun createActivity(activity: Activity): ValidationResult<Activity> = withContext(dispatcherIO) {
        try {
            val docRef = collection.add(activity.toDto()).await()
            val activityId = docRef.id

            val savedActivity = activity.copy(id = activityId)
            ValidationResult(
                data = savedActivity,
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
        } catch (e: FirebaseFirestoreException) {
            ValidationResult(
                error = ErrorType.CommonError(e.message ?: "")
            )
        }
    }
}
