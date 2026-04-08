package ru.tbank.petcare.presentation.screen.petProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.usecase.pets.GetLocalPetUseCase
import ru.tbank.petcare.domain.usecase.pets.GetPetInfoUseCase
import ru.tbank.petcare.presentation.mapper.toForm
import ru.tbank.petcare.utils.ErrorParser

private const val PET_ID = "pet_id"

@HiltViewModel(assistedFactory = PetProfileViewModel.Factory::class)
class PetProfileViewModel @AssistedInject constructor(
    private val getPetUseCase: GetLocalPetUseCase,
    private val getPetInfoUseCase: GetPetInfoUseCase,
    private val errorParser: ErrorParser,
    @Assisted(PET_ID) private val petId: String,
) : ViewModel() {

    private val _state = MutableStateFlow(PetProfileState())
    val state = _state.asStateFlow()

    init {
        loadPet()
    }

    private fun loadPet() {
        viewModelScope.launch {
            getPetUseCase(petId).collect { pet ->
                _state.update { state ->
                    state.copy(
                        petProfileUIModel = pet.toForm()
                    )
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(PET_ID) petId: String
        ): PetProfileViewModel
    }

    fun processCommand(command: PetProfileCommand) {
        when (command) {
            is PetProfileCommand.ShowPetInfo -> {
                viewModelScope.launch {
                    _state.update { state ->
                        state.copy(
                            isInfoLoading = true,
                            errorMessage = null
                        )
                    }
                    val petInfo = getPetInfoUseCase(command.breed)

                    if (petInfo.isSuccess && petInfo.data != null) {
                        _state.update { state ->
                            state.copy(
                                petInfoUIModel = petInfo.data,
                                isInfoLoading = false
                            )
                        }
                    } else {
                        val errorMessage = errorParser.getErrorMessage(petInfo.error)
                        _state.update { state ->
                            state.copy(
                                isInfoLoading = false,
                                errorMessage = errorMessage
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed interface PetProfileCommand {
    data class ShowPetInfo(val breed: String) : PetProfileCommand
}
