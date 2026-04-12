package ru.tbank.petcare.domain.usecase.settings

import ru.tbank.petcare.domain.model.NotificationSettings
import ru.tbank.petcare.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateNotificationsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(notifications: NotificationSettings) {
        settingsRepository.updateNotifications(notifications)
    }
}
