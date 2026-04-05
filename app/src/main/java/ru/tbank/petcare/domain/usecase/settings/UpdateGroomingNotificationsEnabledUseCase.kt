package ru.tbank.petcare.domain.usecase.settings

import ru.tbank.petcare.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateGroomingNotificationsEnabledUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        settingsRepository.updateGroomingNotifications(enabled)
    }
}
