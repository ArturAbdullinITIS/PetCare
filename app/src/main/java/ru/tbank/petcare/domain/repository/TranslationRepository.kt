package ru.tbank.petcare.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.tbank.petcare.domain.model.Language

interface TranslationRepository {
    fun translate(text: String, sourceLanguage: Language, targetLanguage: Language): Flow<String>
}
