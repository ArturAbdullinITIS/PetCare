package ru.tbank.petcare.presentation.screen.editpet

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
import ru.tbank.petcare.domain.model.Gender
import ru.tbank.petcare.domain.model.IconStatus
import ru.tbank.petcare.domain.usecase.EditPetUseCase
import ru.tbank.petcare.domain.usecase.GetPetUseCase
import ru.tbank.petcare.presentation.mapper.toDomain
import ru.tbank.petcare.presentation.mapper.toForm

@HiltViewModel(assistedFactory = EditPetViewModel.Factory::class)
class EditPetViewModel @AssistedInject constructor(
    @Assisted("pet_id") private val petId: String,
    private val getPetUseCase: GetPetUseCase,
    private val editPetUseCase: EditPetUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EditPetState())
    val state = _state.asStateFlow()

    init {
        loadPet()
    }

    private fun loadPet() {
        viewModelScope.launch {
            getPetUseCase(petId).collect { pet ->
                _state.value = EditPetState(petUIModel = pet.toForm())
            }
        }
    }

    fun processCommand(command: EditPetCommand) {
        when (command) {
            EditPetCommand.EditPet -> {
                val pet = state.value.petUIModel.toDomain()
                viewModelScope.launch {
                    editPetUseCase(pet)
                }
            }
            is EditPetCommand.ChangeGender -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(gender = command.gender)
                    )
                }
            }
            is EditPetCommand.ChangeIconStatus -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(iconStatus = command.iconStatus)
                    )
                }
            }
            is EditPetCommand.InputBreed -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(breed = command.breed)
                    )
                }
            }
            is EditPetCommand.InputDateOfBirth -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(dateOfBirth = command.dateOfBirth)
                    )
                }
            }
            is EditPetCommand.InputName -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(name = command.name)
                    )
                }
            }
            is EditPetCommand.InputNotes -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(note = command.note)
                    )
                }
            }
            is EditPetCommand.InputWeight -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(weight = command.weight)
                    )
                }
            }
            EditPetCommand.IsPublic -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(isPublic = !state.petUIModel.isPublic)
                    )
                }
            }

            is EditPetCommand.AddPhotoUrl -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(photoUrl = command.url)
                    )
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("pet_id") petId: String
        ): EditPetViewModel
    }
}

sealed interface EditPetCommand {
    data class InputName(val name: String) : EditPetCommand
    data class ChangeIconStatus(val iconStatus: IconStatus) : EditPetCommand
    data class InputBreed(val breed: String) : EditPetCommand
    data class InputWeight(val weight: String) : EditPetCommand
    data class InputDateOfBirth(val dateOfBirth: Long) : EditPetCommand
    data class ChangeGender(val gender: Gender) : EditPetCommand
    data class InputNotes(val note: String) : EditPetCommand
    data class AddPhotoUrl(val url: String) : EditPetCommand
    object IsPublic : EditPetCommand
    object EditPet : EditPetCommand
}
