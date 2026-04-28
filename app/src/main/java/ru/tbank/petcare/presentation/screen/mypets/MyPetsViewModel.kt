package ru.tbank.petcare.presentation.screen.mypets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.usecase.pets.GetAllPetsUseCase
import ru.tbank.petcare.domain.usecase.pets.GetAllTipsUseCase
import ru.tbank.petcare.domain.usecase.pets.IsOnlineUseCase
import ru.tbank.petcare.domain.usecase.pets.SyncPetsUseCase
import ru.tbank.petcare.domain.usecase.settings.GetAllSettingsUseCase
import ru.tbank.petcare.presentation.mapper.toPetCardUIModel
import ru.tbank.petcare.utils.ResourceProvider
import javax.inject.Inject

@HiltViewModel
class MyPetsViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val getAllPetsUseCase: GetAllPetsUseCase,
    private val getAllTipsUseCase: GetAllTipsUseCase,
    private val syncPetsUseCase: SyncPetsUseCase,
    private val getAllSettingsUseCase: GetAllSettingsUseCase,
    private val isOnlineUseCase: IsOnlineUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MyPetsState())
    private var tipsJob: Job? = null
    val state = _state.asStateFlow()
    val isOnline = isOnlineUseCase()

    init {
        loadPets()
        loadTips()
        viewModelScope.launch {
            getAllSettingsUseCase()
                .collect { settings ->
                    loadTips()
                }
        }
        viewModelScope.launch {
            syncPetsUseCase()
        }
    }

    private fun loadPets() {
        viewModelScope.launch {
            _state.update { it.copy(isPetsLoading = true, errorMessage = null) }

            getAllPetsUseCase()
                .catch { exception ->
                    _state.update {
                        it.copy(
                            isPetsLoading = false,
                            errorMessage = exception.message
                                ?: resourceProvider.getString(R.string.unknown_error_mypets)
                        )
                    }
                }
                .collect { pets ->
                    val uiPets = pets.map { it.toPetCardUIModel() }
                    _state.update {
                        it.copy(
                            pets = uiPets,
                            isPetsLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    private fun loadTips() {
        tipsJob?.cancel()

        tipsJob = viewModelScope.launch {
            _state.update { it.copy(isTipsLoading = true) }

            getAllTipsUseCase(getAllSettingsUseCase().first().language)
                .catch { e ->
                    _state.update {
                        it.copy(
                            isTipsLoading = false,
                            errorMessage = e.message
                                ?: resourceProvider.getString(R.string.unknown_error_mypets)
                        )
                    }
                }
                .collect { tips ->
                    val list = tips.shuffled()
                    _state.update {
                        it.copy(
                            isTipsLoading = false,
                            tips = list,
                            currentTipIndex = 0,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            if (!isOnlineUseCase().value) return@launch

            _state.update { it.copy(isRefreshing = true) }
            syncPetsUseCase()
            _state.update { it.copy(isRefreshing = false) }
        }
    }

    fun nextTip() {
        _state.update { state ->
            val size = state.tips.size
            if (size == 0) {
                state
            } else {
                state.copy(currentTipIndex = (state.currentTipIndex + 1) % size)
            }
        }
    }
}
