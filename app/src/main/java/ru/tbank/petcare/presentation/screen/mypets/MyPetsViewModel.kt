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
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.usecase.GetAllPetsUseCase
import ru.tbank.petcare.utils.ResourceProvider
import javax.inject.Inject

@HiltViewModel
class MyPetsViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val getAllPetsUseCase: GetAllPetsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MyPetsState())
    val state = _state.asStateFlow()

    init {
        loadPets()
    }

    private fun loadPets() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            getAllPetsUseCase()
                .catch { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: resourceProvider.getString(R.string.unknown_error_mypets)
                        )
                    }
                }
                .collect { pets ->
                    _state.update {
                        it.copy(
                            pets = pets,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    private fun refresh() {
        loadPets()
    }

}

data class MyPetsState(
    val pets: List<Pet> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)