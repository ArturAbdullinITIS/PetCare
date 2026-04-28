package ru.tbank.petcare.domain.usecase.common

import kotlinx.coroutines.flow.Flow
import ru.tbank.petcare.domain.model.Language
import ru.tbank.petcare.domain.repository.TranslationRepository
import javax.inject.Inject

class TranslateTextUseCase @Inject constructor(
    private val translationRepository: TranslationRepository
) {
    suspend operator fun invoke(text: String, sourceLanguage: Language, targetLanguage: Language): Flow<String> {
        return translationRepository.translate(text, sourceLanguage, targetLanguage)
    }
}
