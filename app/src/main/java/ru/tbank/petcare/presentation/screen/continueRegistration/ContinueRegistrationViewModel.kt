package ru.tbank.petcare.presentation.screen.continueRegistration

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
import ru.tbank.petcare.domain.usecase.AddUserUseCase
import ru.tbank.petcare.domain.usecase.UploadUsersPhotoUseCase
import ru.tbank.petcare.presentation.mapper.toDomain
import ru.tbank.petcare.utils.ResourceProvider
import javax.inject.Inject

@HiltViewModel
class ContinueRegistrationViewModel @Inject constructor(
    private val addUserUseCase: AddUserUseCase,
    private val uploadUsersPhotoUseCase: UploadUsersPhotoUseCase,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private val _state = MutableStateFlow(ContinueRegistrationState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<ContinueRegistrationEvent>()
    val events = _events.asSharedFlow()

    @Suppress("TooGenericExceptionCaught")
    fun processCommand(command: ContinueRegistrationCommand) {
        when (command) {
            is ContinueRegistrationCommand.InputFirstName -> {
                _state.update { state ->
                    state.copy(
                        user = state.user.copy(firstName = command.firstName)
                    )
                }
            }
            is ContinueRegistrationCommand.InputLastName -> {
                _state.update { state ->
                    state.copy(
                        user = state.user.copy(lastName = command.lastName)
                    )
                }
            }
            ContinueRegistrationCommand.Save -> {
                val uri = state.value.selectedPhotoUri
                val user = state.value.user.toDomain()
                _state.update { state ->
                    state.copy(
                        isSaving = true
                    )
                }
                viewModelScope.launch {
                    try {
                        val photoUrl = if (uri != null) {
                            val upload = uploadUsersPhotoUseCase(uri)
                            if (!upload.isSuccess || upload.data == null) {
                                _events.emit(
                                    ContinueRegistrationEvent.Error(
                                        message = resourceProvider.getString(R.string.could_not_upload_photo)
                                    )
                                )
                                return@launch
                            }
                            upload.data
                        } else {
                            ""
                        }
                        val result = addUserUseCase(user.copy(photoUrl = photoUrl))
                        if (result.isSuccess) {
                            _events.emit(
                                ContinueRegistrationEvent.Saved
                            )
                        } else {
                            _events.emit(
                                ContinueRegistrationEvent.Error(
                                    message = resourceProvider.getString(R.string.could_not_save_user)
                                )
                            )
                        }
                    } catch (t: Throwable) {
                        _events.emit(
                            ContinueRegistrationEvent.Error(
                                message = "Unknown registration error: ${t.localizedMessage ?: t.toString()}"
                            )
                        )
                    } finally {
                        _state.update { state ->
                            state.copy(
                                isSaving = false
                            )
                        }
                    }
                }
            }
            is ContinueRegistrationCommand.SetPhoto -> {
                _state.update { state ->
                    state.copy(
                        selectedPhotoUri = command.uri
                    )
                }
            }
        }
    }
}

sealed interface ContinueRegistrationCommand {
    object Save : ContinueRegistrationCommand
    data class InputFirstName(val firstName: String) : ContinueRegistrationCommand
    data class InputLastName(val lastName: String) : ContinueRegistrationCommand
    data class SetPhoto(val uri: Uri) : ContinueRegistrationCommand
}
