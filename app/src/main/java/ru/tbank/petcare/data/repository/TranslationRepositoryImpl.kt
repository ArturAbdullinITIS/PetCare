package ru.tbank.petcare.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tbank.petcare.data.remote.network.deepl.DeeplApiService
import ru.tbank.petcare.domain.model.Language
import ru.tbank.petcare.domain.repository.TranslationRepository
import javax.inject.Inject

class TranslationRepositoryImpl @Inject constructor(
    private val deeplApiService: DeeplApiService,
) : TranslationRepository {

    private fun getDeeplLanguage(language: Language): String {
        return when (language) {
            Language.ENGLISH -> "EN"
            Language.RUSSIAN -> "RU"
        }
    }

    override fun translate(text: String, sourceLanguage: Language, targetLanguage: Language): Flow<String> = flow {
        if (sourceLanguage == targetLanguage) {
            emit(text)
            return@flow
        }

        try {
            val response = deeplApiService.translate(
                text = text,
                sourceLang = getDeeplLanguage(sourceLanguage),
                targetLang = getDeeplLanguage(targetLanguage)
            )

            val translatedText = response.translations.firstOrNull()?.text ?: text
            emit(translatedText)
        } catch (_: Exception) {
            emit(text)
        }
    }
}
