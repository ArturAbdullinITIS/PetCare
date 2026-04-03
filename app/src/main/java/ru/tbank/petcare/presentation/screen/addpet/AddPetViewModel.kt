package ru.tbank.petcare.presentation.screen.addpet

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.model.Gender
import ru.tbank.petcare.domain.model.IconStatus
import ru.tbank.petcare.domain.usecase.AddPetUseCase
import ru.tbank.petcare.domain.usecase.UploadPetPhotoUseCase
import ru.tbank.petcare.presentation.mapper.toDomain
import ru.tbank.petcare.utils.DateFormatter
import ru.tbank.petcare.utils.ResourceProvider
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val addPetUseCase: AddPetUseCase,
    private val uploadPetPhotoUseCase: UploadPetPhotoUseCase,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _state = MutableStateFlow(AddPetState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<AddPetEvent>()
    val events = _events.asSharedFlow()

    @Suppress("CyclomaticComplexMethod", "TooGenericExceptionCaught", "SwallowedException")
    fun processCommand(command: AddPetCommand) {
        when (command) {
            AddPetCommand.AddPet -> {
                val pet = state.value.petUIModel.toDomain()
                val uri = state.value.selectedPhotoUri

                viewModelScope.launch {
                    _state.update { it.copy(isAdding = true) }

                    try {
                        val photoUrl = if (uri != null) {
                            val upload = uploadPetPhotoUseCase(uri)
                            if (!upload.isSuccess || upload.data == null) {
                                _events.emit(
                                    AddPetEvent.Error(
                                        message = resourceProvider.getString(R.string.could_not_upload_photo)
                                    )
                                )
                                return@launch
                            }
                            upload.data
                        } else {
                            ""
                        }

                        val result = addPetUseCase(pet.copy(photoUrl = photoUrl))
                        if (result.isSuccess) {
                            _events.emit(AddPetEvent.Saved)
                            _state.value = AddPetState()
                        } else {
                            _events.emit(
                                AddPetEvent.Error(
                                    message = resourceProvider.getString(R.string.could_not_save_pet)
                                )
                            )
                        }
                    } catch (t: Throwable) {
                        _events.emit(
                            AddPetEvent.Error(
                                message = resourceProvider.getString(R.string.unknown_error_mypets)
                            )
                        )
                    } finally {
                        _state.update { it.copy(isAdding = false) }
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
                val normalized = command.dateOfBirth?.let { DateFormatter.normalizeToStartOfDayUtc(it) }
                val text = DateFormatter.formatDob(normalized)

                _state.update { state ->
                    state.copy(
                        petUIModel = state.petUIModel.copy(
                            dateOfBirth = normalized,
                            dateOfBirthText = text
                        )
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
    data class InputDateOfBirth(val dateOfBirth: Date?) : AddPetCommand
    data class ChangeGender(val gender: Gender) : AddPetCommand
    data class InputNotes(val note: String) : AddPetCommand
    data class SelectPhoto(val uri: Uri) : AddPetCommand
    object IsPublic : AddPetCommand
    object AddPet : AddPetCommand
}
