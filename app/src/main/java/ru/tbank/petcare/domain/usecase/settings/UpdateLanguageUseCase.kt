package ru.tbank.petcare.domain.usecase.settings

import ru.tbank.petcare.domain.model.Language
import ru.tbank.petcare.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(language: Language) {
        settingsRepository.updateLanguage(language)
    }
}
