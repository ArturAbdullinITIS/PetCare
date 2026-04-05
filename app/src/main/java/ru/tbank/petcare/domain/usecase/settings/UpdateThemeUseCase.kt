package ru.tbank.petcare.domain.usecase.settings

import ru.tbank.petcare.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(darkTheme: Boolean) {
        settingsRepository.updateTheme(darkTheme)
    }
}
