package ru.tbank.petcare.presentation.screen.petProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.usecase.GetPetInfoUseCase
import ru.tbank.petcare.domain.usecase.GetPetUseCase
import ru.tbank.petcare.presentation.model.PetForm
import ru.tbank.petcare.utils.ErrorParser

private const val PET_ID = "pet_id"

@HiltViewModel(assistedFactory = PetProfileViewModel.Factory::class)
class PetProfileViewModel @AssistedInject constructor(
    private val getPetUseCase: GetPetUseCase,
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
                        petProfileUIModel = PetForm(
                            name = pet.name,
                            breed = pet.breed,
                            gender = pet.gender,
                            isPublic = pet.isPublic,
                            note = pet.note,
                            weight = pet.weight.toString(),
                            dateOfBirth = pet.dateOfBirth,
                            iconStatus = pet.iconStatus,
                            photoUrl = pet.photoUrl
                        )
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
                    val petInfo = getPetInfoUseCase(command.breed)
                    if (petInfo.isSuccess && petInfo.data != null) {
                        _state.update { state ->
                            state.copy(
                                petInfoUIModel = petInfo.data
                            )
                        }
                    } else {
                        val errorMessage = errorParser.getErrorMessage(petInfo.error)
                        _state.update { state ->
                            state.copy(
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
