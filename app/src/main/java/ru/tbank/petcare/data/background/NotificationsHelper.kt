package ru.tbank.petcare.data.background

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.tbank.petcare.R
import javax.inject.Inject

class NotificationsHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private companion object {
        const val GROOMING_CHANNEL_ID = "grooming"
        const val WALK_CHANNEL_ID = "walk"
        const val VET_CHANNEL_ID = "vet"
    }
    private val notificationManager = context.getSystemService<NotificationManager>()

    init {
        createNotificationChannels()
    }

    private fun generateId(type: String): Int {
        return "$type${System.currentTimeMillis()}".hashCode()
    }

    private fun createNotificationChannels() {
        val channels = listOf(
            NotificationChannel(
                GROOMING_CHANNEL_ID,
                context.getString(R.string.grooming),
                NotificationManager.IMPORTANCE_DEFAULT
            ),
            NotificationChannel(
                WALK_CHANNEL_ID,
                context.getString(R.string.walk),
                NotificationManager.IMPORTANCE_DEFAULT
            ),
            NotificationChannel(
                VET_CHANNEL_ID,
                context.getString(R.string.veterinarian),
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
        notificationManager?.createNotificationChannels(channels)
    }

    fun showGroomingNotification(petName: String) {
        val groomingNotification = NotificationCompat.Builder(context, GROOMING_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_pet_care_main)
            .setContentTitle(context.getString(R.string.grooming_reminder))
            .setContentText(context.getString(R.string.grooming_session_for_is_coming, petName))
            .setAutoCancel(true)
            .build()
        notificationManager?.notify(generateId(GROOMING_CHANNEL_ID), groomingNotification)
    }

    fun showWalkNotification(petName: String) {
        val walkNotification = NotificationCompat.Builder(context, WALK_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_pet_care_main)
            .setContentTitle(context.getString(R.string.walk_reminder))
            .setContentText(context.getString(R.string.don_t_forget_to_walk, petName))
            .setAutoCancel(true)
            .build()
        notificationManager?.notify(generateId(WALK_CHANNEL_ID), walkNotification)
    }

    fun showVetNotification(petName: String) {
        val vetNotification = NotificationCompat.Builder(context, VET_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_pet_care_main)
            .setContentTitle(context.getString(R.string.vet_reminder))
            .setContentText(context.getString(R.string.vet_visit_for_is_coming, petName))
            .setAutoCancel(true)
            .build()
        notificationManager?.notify(generateId(VET_CHANNEL_ID), vetNotification)
    }
}
