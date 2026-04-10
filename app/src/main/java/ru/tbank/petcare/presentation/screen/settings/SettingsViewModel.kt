package ru.tbank.petcare.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.model.Language
import ru.tbank.petcare.domain.model.Settings
import ru.tbank.petcare.domain.usecase.settings.GetAllSettingsUseCase
import ru.tbank.petcare.domain.usecase.settings.UpdateAllNotificationsEnabledUseCase
import ru.tbank.petcare.domain.usecase.settings.UpdateGroomingNotificationsEnabledUseCase
import ru.tbank.petcare.domain.usecase.settings.UpdateLanguageUseCase
import ru.tbank.petcare.domain.usecase.settings.UpdateThemeUseCase
import ru.tbank.petcare.domain.usecase.settings.UpdateVetNotificationsEnabledUseCase
import ru.tbank.petcare.domain.usecase.settings.UpdateWalkNotificationsEnabledUseCase
import ru.tbank.petcare.domain.usecase.users.DeleteCurrentUserUseCase
import javax.inject.Inject

@Suppress("LongParameterList")
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getAllSettingsUseCase: GetAllSettingsUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val updateAllNotificationsEnabledUseCase: UpdateAllNotificationsEnabledUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase,
    private val updateVetNotificationsEnabledUseCase: UpdateVetNotificationsEnabledUseCase,
    private val updateGroomingNotificationsEnabledUseCase: UpdateGroomingNotificationsEnabledUseCase,
    private val updateWalkNotificationsEnabledUseCase: UpdateWalkNotificationsEnabledUseCase,
    private val deleteCurrentUserUseCase: DeleteCurrentUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()
    private val _events = MutableSharedFlow<SettingsEvent>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            getAllSettingsUseCase().collect { settings ->
                val ruSelected = settings.language == Language.RUSSIAN
                val enSelected = settings.language == Language.ENGLISH
                _state.update { state ->
                    state.copy(
                        settingsConfig = Settings(
                            language = settings.language,
                            darkTheme = settings.darkTheme,
                            notifications = settings.notifications
                        ),
                        languageState = LanguageState(
                            selected = settings.language,
                            ruSelected = ruSelected,
                            enSelected = enSelected
                        )
                    )
                }
            }
        }
    }

    fun processCommand(command: SettingsCommand) {
        viewModelScope.launch {
            when (command) {
                is SettingsCommand.ChangeLanguage -> {
                    updateLanguageUseCase(command.language)
                }

                is SettingsCommand.EnableAllNotifications -> {
                    updateAllNotificationsEnabledUseCase(command.enable)
                }

                is SettingsCommand.EnableDarkTheme -> {
                    updateThemeUseCase(command.enable)
                }

                is SettingsCommand.EnableGroomingNotifications -> {
                    updateGroomingNotificationsEnabledUseCase(command.enable)
                }

                is SettingsCommand.EnableVetNotifications -> {
                    updateVetNotificationsEnabledUseCase(command.enable)
                }

                is SettingsCommand.EnableWalkNotifications -> {
                    updateWalkNotificationsEnabledUseCase(command.enable)
                }

                SettingsCommand.DeleteProfile -> deleteProfile()
            }
        }
    }

    private fun deleteProfile() {
        viewModelScope.launch {
            val result = deleteCurrentUserUseCase()
            if (result.isSuccess) {
                _events.emit(SettingsEvent.ProfileDeleted)
            } else {
                _events.emit(SettingsEvent.Error(""))
            }
        }
    }
}

sealed interface SettingsCommand {
    data class EnableAllNotifications(val enable: Boolean) : SettingsCommand
    data class EnableGroomingNotifications(val enable: Boolean) : SettingsCommand
    data class EnableVetNotifications(val enable: Boolean) : SettingsCommand
    data class EnableWalkNotifications(val enable: Boolean) : SettingsCommand
    data class EnableDarkTheme(val enable: Boolean) : SettingsCommand
    data class ChangeLanguage(val language: Language) : SettingsCommand
    object DeleteProfile : SettingsCommand
}
