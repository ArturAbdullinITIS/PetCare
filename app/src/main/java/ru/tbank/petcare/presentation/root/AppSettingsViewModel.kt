package ru.tbank.petcare.presentation.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.model.Settings
import ru.tbank.petcare.domain.usecase.settings.ApplyLocaleUseCase
import ru.tbank.petcare.domain.usecase.settings.GetAllSettingsUseCase
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val getAllSettingsUseCase: GetAllSettingsUseCase,
    private val applyLocaleUseCase: ApplyLocaleUseCase
) : ViewModel() {

    private val _settings = MutableStateFlow(Settings())
    val settings = _settings.asStateFlow()

    init {
        viewModelScope.launch {
            getAllSettingsUseCase()
                .onEach { _settings.value = it }
                .distinctUntilChangedBy { it.language }
                .onEach { settings -> applyLocaleUseCase(settings.language) }
                .collect()
        }
    }
}
