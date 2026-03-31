package ru.tbank.petcare.presentation.screen.mypets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.usecase.GetAllPetsUseCase
import ru.tbank.petcare.domain.usecase.GetAllTipsUseCase
import ru.tbank.petcare.utils.ResourceProvider
import javax.inject.Inject

@HiltViewModel
class MyPetsViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val getAllPetsUseCase: GetAllPetsUseCase,
    private val getAllTipsUseCase: GetAllTipsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MyPetsState())
    val state = _state.asStateFlow()

    init {
        loadPets()
        loadTips()
    }

    private fun loadPets() {
        viewModelScope.launch {
            _state.update { it.copy(isPetsLoading = true, errorMessage = null) }

            getAllPetsUseCase()
                .catch { exception ->
                    _state.update {
                        it.copy(
                            isPetsLoading = false,
                            errorMessage = exception.message ?: resourceProvider.getString(R.string.unknown_error_mypets)
                        )
                    }
                }
                .collect { pets ->
                    _state.update {
                        it.copy(
                            pets = pets,
                            isPetsLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    private fun loadTips() {
        viewModelScope.launch {
            _state.update { it.copy(isTipsLoading = true) }

            getAllTipsUseCase()
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
        loadPets()
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
