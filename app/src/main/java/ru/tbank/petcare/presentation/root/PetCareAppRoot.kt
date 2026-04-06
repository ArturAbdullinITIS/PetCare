package ru.tbank.petcare.presentation.root
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.tbank.petcare.domain.model.Language
import ru.tbank.petcare.presentation.navigation.NavHost
import ru.tbank.petcare.presentation.ui.theme.PetCareTheme

private const val TAG_RU = "ru"
private const val TAG_EN = "en"
private const val SDK_VERSION = 33

@Composable
fun PetCareAppRoot() {
    val vm: AppSettingsViewModel = hiltViewModel()
    val settings by vm.settings.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val tag = when (settings.language) {
        Language.RUSSIAN -> TAG_RU
        Language.ENGLISH -> TAG_EN
    }

    LaunchedEffect(tag) {
        if (Build.VERSION.SDK_INT >= SDK_VERSION) {
            val localeManager =
                androidx.core.content.ContextCompat.getSystemService(
                    context,
                    android.app.LocaleManager::class.java
                )
            localeManager?.applicationLocales = android.os.LocaleList.forLanguageTags(tag)
        } else {
            androidx.appcompat.app.AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(tag)
            )
        }
    }

    PetCareTheme(darkTheme = settings.darkTheme) {
        NavHost()
    }
}
