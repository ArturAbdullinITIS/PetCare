package ru.tbank.petcare.domain.usecase.settings

import ru.tbank.petcare.domain.model.Language
import ru.tbank.petcare.domain.repository.LocaleRepository
import javax.inject.Inject

class ApplyLocaleUseCase @Inject constructor(
    private val localeRepository: LocaleRepository
) {
    operator fun invoke(language: Language) {
        localeRepository.apply(language)
    }
}
