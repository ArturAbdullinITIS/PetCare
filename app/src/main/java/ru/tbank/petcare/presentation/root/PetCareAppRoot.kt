package ru.tbank.petcare.presentation.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.tbank.petcare.presentation.navigation.NavHost
import ru.tbank.petcare.presentation.ui.theme.PetCareTheme

@Composable
fun PetCareAppRoot(
    splashViewModel: SplashViewModel
) {
    val appSettingsViewModel: AppSettingsViewModel = hiltViewModel()
    val settings by appSettingsViewModel.settings.collectAsStateWithLifecycle()
    val startDestination by splashViewModel.startDestination.collectAsState()

    if (startDestination != null) {
        PetCareTheme(darkTheme = settings.darkTheme) {
            NavHost(
                startDestination = startDestination!!,
            )
        }
    }
}
