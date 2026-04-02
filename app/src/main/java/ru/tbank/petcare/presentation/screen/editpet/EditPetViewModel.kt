package ru.tbank.petcare.presentation.screen.editpet

import android.net.Uri
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
import ru.tbank.petcare.domain.usecase.DeletePetUseCase
import ru.tbank.petcare.domain.usecase.EditPetUseCase
import ru.tbank.petcare.domain.usecase.GetPetUseCase
import ru.tbank.petcare.domain.usecase.UploadPetPhotoUseCase
import ru.tbank.petcare.presentation.mapper.toDomain
import ru.tbank.petcare.presentation.mapper.toForm

@HiltViewModel(assistedFactory = EditPetViewModel.Factory::class)
class EditPetViewModel @AssistedInject constructor(
    @Assisted("pet_id") private val petId: String,
    private val getPetUseCase: GetPetUseCase,
    private val editPetUseCase: EditPetUseCase,
    private val uploadPetPhotoUseCase: UploadPetPhotoUseCase,
    private val deletePetUseCase: DeletePetUseCase
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
            EditPetCommand.EditPet -> handleEditPet()
            EditPetCommand.DeletePetProfile -> handleDeletePet()
            EditPetCommand.IsPublic -> toggleIsPublic()
            EditPetCommand.ShowDeleteDialog -> toggleDeleteDialog()

            is EditPetCommand.SelectPhoto -> setSelectedPhoto(command.uri)
            is EditPetCommand.InputName -> setName(command.name)
            is EditPetCommand.InputBreed -> setBreed(command.breed)
            is EditPetCommand.InputWeight -> setWeight(command.weight)
            is EditPetCommand.InputDateOfBirth -> setDateOfBirth(command.dateOfBirth)
            is EditPetCommand.InputNotes -> setNote(command.note)
            is EditPetCommand.ChangeGender -> setGender(command.gender)
            is EditPetCommand.ChangeIconStatus -> setIconStatus(command.iconStatus)
        }
    }

    private fun handleEditPet() {
        val pet = state.value.petUIModel.toDomain()
        val uri = state.value.selectedPhotoUri

        viewModelScope.launch {
            setUploadingPhoto(uri != null)

            val finalPhotoUrl = if (uri != null) {
                val upload = uploadPetPhotoUseCase(uri)
                if (!upload.isSuccess || upload.data == null) {
                    setUploadingPhoto(false)
                    return@launch
                }
                upload.data
            } else {
                pet.photoUrl
            }

            editPetUseCase(pet.copy(photoUrl = finalPhotoUrl))
            setUploadingPhoto(false)
        }
    }

    private fun handleDeletePet() {
        viewModelScope.launch {
            deletePetUseCase(petId)
        }
    }

    private fun setUploadingPhoto(value: Boolean) {
        _state.update { it.copy(isUploadingPhoto = value) }
    }

    private fun toggleIsPublic() {
        _state.update { state ->
            state.copy(petUIModel = state.petUIModel.copy(isPublic = !state.petUIModel.isPublic))
        }
    }

    private fun toggleDeleteDialog() {
        _state.update { it.copy(showDeleteDialog = !it.showDeleteDialog) }
    }

    private fun setSelectedPhoto(uri: Uri) {
        _state.update { it.copy(selectedPhotoUri = uri) }
    }

    private fun setName(name: String) {
        _state.update { it.copy(petUIModel = it.petUIModel.copy(name = name)) }
    }

    private fun setBreed(breed: String) {
        _state.update { it.copy(petUIModel = it.petUIModel.copy(breed = breed)) }
    }

    private fun setWeight(weight: String) {
        _state.update { it.copy(petUIModel = it.petUIModel.copy(weight = weight)) }
    }

    private fun setDateOfBirth(dateOfBirth: Long) {
        _state.update { it.copy(petUIModel = it.petUIModel.copy(dateOfBirth = dateOfBirth)) }
    }

    private fun setNote(note: String) {
        _state.update { it.copy(petUIModel = it.petUIModel.copy(note = note)) }
    }

    private fun setGender(gender: Gender) {
        _state.update { it.copy(petUIModel = it.petUIModel.copy(gender = gender)) }
    }

    private fun setIconStatus(iconStatus: IconStatus) {
        _state.update { it.copy(petUIModel = it.petUIModel.copy(iconStatus = iconStatus)) }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("pet_id") petId: String): EditPetViewModel
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
    data class SelectPhoto(val uri: Uri) : EditPetCommand
    object IsPublic : EditPetCommand
    object EditPet : EditPetCommand
    object DeletePetProfile : EditPetCommand
    object ShowDeleteDialog : EditPetCommand
}
