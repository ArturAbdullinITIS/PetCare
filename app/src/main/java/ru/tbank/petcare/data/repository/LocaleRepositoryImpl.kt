package ru.tbank.petcare.data.repository

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.tbank.petcare.domain.model.Language
import ru.tbank.petcare.domain.repository.LocaleRepository
import javax.inject.Inject

class LocaleRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocaleRepository {
    override fun apply(language: Language) {
        val tag = language.tag

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = ContextCompat.getSystemService(context, LocaleManager::class.java)
            localeManager?.applicationLocales = LocaleList.forLanguageTags(tag)
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
        }
    }
}
