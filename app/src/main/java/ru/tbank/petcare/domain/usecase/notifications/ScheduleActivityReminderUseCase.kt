package ru.tbank.petcare.domain.usecase.notifications

import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import ru.tbank.petcare.data.background.NotificationsWorker
import ru.tbank.petcare.domain.model.ActivityType
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScheduleActivityReminderUseCase @Inject constructor(
    private val workManager: WorkManager
) {
    fun schedule(
        petId: String,
        type: ActivityType,
        activityDate: Date
    ) {
        val reminderDelayDays = when (type) {
            ActivityType.WALK -> 1L
            ActivityType.GROOMING -> 30L
            ActivityType.VET -> 30L
        }

        val reminderTimeMillis = activityDate.time + TimeUnit.DAYS.toMillis(reminderDelayDays)
        val currentTimeMillis = System.currentTimeMillis()

        if (reminderTimeMillis <= currentTimeMillis) {
            return
        }

        val delayMillis = reminderTimeMillis - currentTimeMillis

        val input = Data.Builder()
            .putString(NotificationsWorker.TYPE_KEY, type.name.lowercase())
            .putString(NotificationsWorker.PET_ID_KEY, petId)
            .putLong(NotificationsWorker.ACTIVITY_DATE_KEY, activityDate.time)
            .build()

        val request = OneTimeWorkRequestBuilder<NotificationsWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(input)
            .build()

        val uniqueName = "reminder_${petId}_${type.name}_${activityDate.time}"
        workManager.enqueueUniqueWork(uniqueName, ExistingWorkPolicy.REPLACE, request)
    }

    fun cancel(petId: String, activityType: ActivityType, activityDate: Date? = null) {
        val uniqueName = if (activityDate != null) {
            "reminder_${petId}_${activityType.name}_${activityDate.time}"
        } else {
            "reminder_${petId}_${activityType.name}"
        }
        workManager.cancelUniqueWork(uniqueName)
    }
}
