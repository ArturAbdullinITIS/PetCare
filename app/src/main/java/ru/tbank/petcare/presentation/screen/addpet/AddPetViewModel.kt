package ru.tbank.petcare.presentation.screen.addpet

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.model.Gender
import ru.tbank.petcare.domain.model.IconStatus
import ru.tbank.petcare.domain.usecase.AddPetUseCase
import ru.tbank.petcare.domain.usecase.UploadPetPhotoUseCase
import ru.tbank.petcare.presentation.mapper.toDomain
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val addPetUseCase: AddPetUseCase,
    private val uploadPetPhotoUseCase: UploadPetPhotoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddPetState())
    val state = _state.asStateFlow()

    fun processCommand(command: AddPetCommand) {
        when (command) {
            AddPetCommand.AddPet -> {
                val pet = state.value.petUIModel.toDomain()
                val uri = state.value.selectedPhotoUri

                viewModelScope.launch {
                    _state.update { state ->
                        state.copy(
                            isUploadingPhoto = uri != null
                        )
                    }

                    val photoUrl = if (uri != null) {
                        val upload = uploadPetPhotoUseCase(uri)
                        if (!upload.isSuccess || upload.data == null) {
                            _state.update { state ->
                                state.copy(
                                    isUploadingPhoto = false
                                )
                            }
                            return@launch
                        }
                        upload.data
                    } else {
                        ""
                    }

                    addPetUseCase(pet.copy(photoUrl = photoUrl))
                    _state.update { state ->
                        state.copy(
                            isUploadingPhoto = false
                        )
                    }
                }
            }
            is AddPetCommand.ChangeGender -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(gender = command.gender)
                    )
                }
            }
            is AddPetCommand.ChangeIconStatus -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(iconStatus = command.iconStatus)
                    )
                }
            }
            is AddPetCommand.InputBreed -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(breed = command.breed)
                    )
                }
            }
            is AddPetCommand.InputDateOfBirth -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(dateOfBirth = command.dateOfBirth)
                    )
                }
            }
            is AddPetCommand.InputName -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(name = command.name)
                    )
                }
            }
            is AddPetCommand.InputNotes -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(note = command.note)
                    )
                }
            }
            is AddPetCommand.InputWeight -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(weight = command.weight)
                    )
                }
            }
            AddPetCommand.IsPublic -> {
                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(isPublic = !state.petUIModel.isPublic)
                    )
                }
            }

            is AddPetCommand.SelectPhoto -> {
                _state.update { state ->
                    state.copy(
                        selectedPhotoUri = command.uri
                    )
                }
            }
        }
    }
}

sealed interface AddPetCommand {
    data class InputName(val name: String) : AddPetCommand
    data class ChangeIconStatus(val iconStatus: IconStatus) : AddPetCommand
    data class InputBreed(val breed: String) : AddPetCommand
    data class InputWeight(val weight: String) : AddPetCommand
    data class InputDateOfBirth(val dateOfBirth: Long) : AddPetCommand
    data class ChangeGender(val gender: Gender) : AddPetCommand
    data class InputNotes(val note: String) : AddPetCommand
    data class SelectPhoto(val uri: Uri) : AddPetCommand
    object IsPublic : AddPetCommand
    object AddPet : AddPetCommand
}
