package ru.tbank.petcare.data.background

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.tbank.petcare.data.local.PetsDao

@HiltWorker
class NotificationsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val notificationsHelper: NotificationsHelper,
    private val petsDao: PetsDao
) : CoroutineWorker(context, workerParameters) {
    companion object {
        const val TYPE_KEY = "type"
        const val PET_ID_KEY = "pet_id"
        const val ACTIVITY_DATE_KEY = "activity_date"
        private const val WALK = "walk"
        private const val VET = "vet"
        private const val GROOMING = "grooming"
    }
    override suspend fun doWork(): Result {
        val type = inputData.getString(TYPE_KEY)
        val petId = inputData.getString(PET_ID_KEY)
        val pet = petId?.let { petsDao.getPetByIdOnce(it) }

        return if (type != null && pet != null) {
            when (type.lowercase()) {
                WALK -> notificationsHelper.showWalkNotification(pet.name)
                VET -> notificationsHelper.showVetNotification(pet.name)
                GROOMING -> notificationsHelper.showGroomingNotification(pet.name)
                else -> Result.failure()
            }
            Result.success()
        } else {
            Result.failure()
        }
    }
}
