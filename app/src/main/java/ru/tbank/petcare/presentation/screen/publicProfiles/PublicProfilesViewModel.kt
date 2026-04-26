package ru.tbank.petcare.presentation.screen.publicProfiles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.usecase.pets.GetAllPublicPetsUseCase
import ru.tbank.petcare.domain.usecase.pets.IsOnlineUseCase
import ru.tbank.petcare.presentation.mapper.toPublicPetCardUIModel
import ru.tbank.petcare.presentation.model.PublicProfilesSortOption
import javax.inject.Inject

@HiltViewModel
class PublicProfilesViewModel @Inject constructor(
    private val getAllPublicPetsUseCase: GetAllPublicPetsUseCase,
    private val isOnlineUseCase: IsOnlineUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PublicProfilesState())
    val state = _state.asStateFlow()

    val isOnline: StateFlow<Boolean> = isOnlineUseCase()

    init {
        loadPublicPets()
    }

    fun processCommand(command: PublicProfilesCommand) {
        when (command) {
            is PublicProfilesCommand.ChooseSortOption -> {
                _state.update { state ->
                    state.copy(
                        sortOption = command.option
                    )
                }
            }
        }
    }

    private fun loadPublicPets() {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    isPetsLoading = true,
                    errorMessage = null
                )
            }
            getAllPublicPetsUseCase()
                .catch { exception ->
                    _state.update { state ->
                        state.copy(
                            isPetsLoading = false,
                            errorMessage = exception.message
                        )
                    }
                }
                .collect { pets ->
                    val publicPetsUI =
                        pets.map { it.toPublicPetCardUIModel() }
                    _state.update { state ->
                        state.copy(
                            isPetsLoading = false,
                            pets = publicPetsUI
                        )
                    }
                }
        }
    }
}

sealed interface PublicProfilesCommand {
    data class ChooseSortOption(val option: PublicProfilesSortOption) : PublicProfilesCommand
}
