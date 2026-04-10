package ru.tbank.petcare.domain.repository

import ru.tbank.petcare.domain.model.Language

interface LocaleRepository {
    fun apply(language: Language)
}
