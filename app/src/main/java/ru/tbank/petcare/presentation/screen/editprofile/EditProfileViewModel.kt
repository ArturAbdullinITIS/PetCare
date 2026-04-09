package ru.tbank.petcare.presentation.screen.editprofile

import android.net.Uri
import android.util.Log
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
import ru.tbank.petcare.domain.usecase.users.EditUserUseCase
import ru.tbank.petcare.domain.usecase.users.GetCurrentUserUseCase
import ru.tbank.petcare.domain.usecase.users.UploadUsersPhotoUseCase
import ru.tbank.petcare.presentation.mapper.toDomain
import ru.tbank.petcare.presentation.model.UserForm
import ru.tbank.petcare.presentation.screen.continueRegistration.ContinueRegistrationCommand
import ru.tbank.petcare.presentation.screen.continueRegistration.ContinueRegistrationEvent
import ru.tbank.petcare.utils.ResourceProvider
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val uploadUsersPhotoUseCase: UploadUsersPhotoUseCase,
    private val editUserUseCase: EditUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val resourceProvider: ResourceProvider
): ViewModel(){

    private val _state = MutableStateFlow(EditProfileState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<EditProfileEvent>()
    val events = _events.asSharedFlow()

    init {
        loadUser()
    }


    private fun loadUser() {
            viewModelScope.launch {
                getCurrentUserUseCase().collect { user ->
                    _state.update { state ->
                        state.copy(user = UserForm(id = user.id, firstName = user.firstName, lastName = user.lastName, photoUrl = user.photoUrl, email = user.email,))
                    }
                }
            }
    }

    fun processCommand(command: EditProfileCommand) {
        when (command) {
            is EditProfileCommand.InputFirstName -> {
                _state.update { state ->
                    state.copy(
                        user = state.user.copy(firstName = command.firstName)
                    )
                }
            }
            is EditProfileCommand.InputLastName -> {
                _state.update { state ->
                    state.copy(
                        user = state.user.copy(lastName = command.lastName)
                    )
                }
            }
            EditProfileCommand.Edit -> {
                val uri = state.value.selectedPhotoUri
                val user = state.value.user.toDomain()
                _state.update { state ->
                    state.copy(
                        isEditing = true
                    )
                }
                viewModelScope.launch {
                    try {
                        val photoUrl = if (uri != null) {
                            val upload = uploadUsersPhotoUseCase(uri)
                            if (!upload.isSuccess || upload.data == null) {
                                _events.emit(
                                    EditProfileEvent.Error(
                                        message = resourceProvider.getString(R.string.could_not_upload_photo)
                                    )
                                )
                                return@launch
                            }
                            upload.data
                        } else {
                            ""
                        }
                        val result = editUserUseCase(user.copy(photoUrl = photoUrl))
                        if (result.isSuccess) {
                            _events.emit(
                                EditProfileEvent.Saved
                            )
                        } else {
                            _events.emit(
                                EditProfileEvent.Error(
                                    message = resourceProvider.getString(R.string.could_not_save_user)
                                )
                            )
                        }
                    } catch (t: Throwable) {
                        _events.emit(
                            EditProfileEvent.Error(
                                message = "Unknown editing error: ${t.localizedMessage ?: t.toString()}"
                            )
                        )
                    } finally {
                        _state.update { state ->
                            state.copy(
                                isEditing = false
                            )
                        }
                    }
                }
            }
            is EditProfileCommand.SetPhoto -> {
                _state.update { state ->
                    state.copy(
                        selectedPhotoUri = command.uri
                    )
                }
            }
        }
    }
}

sealed interface EditProfileCommand {
    object Edit : EditProfileCommand
    data class InputFirstName(val firstName: String) : EditProfileCommand
    data class InputLastName(val lastName: String) : EditProfileCommand
    data class SetPhoto(val uri: Uri) : EditProfileCommand
}