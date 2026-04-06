package ru.tbank.petcare.domain.usecase.settings

import kotlinx.coroutines.flow.Flow
import ru.tbank.petcare.domain.model.Settings
import ru.tbank.petcare.domain.repository.SettingsRepository
import javax.inject.Inject

class GetAllSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<Settings> {
        return settingsRepository.getSettings()
    }
}
