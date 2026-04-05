package ru.tbank.petcare.presentation.screen.continueRegistration

import android.net.Uri
import ru.tbank.petcare.presentation.model.UserForm

data class ContinueRegistrationState(
    val isSuccess: Boolean = false,
    val isSaving: Boolean = false,
    val user: UserForm = UserForm(),
    val selectedPhotoUri: Uri? = null,
    val error: String? = null
) {
    val isButtonEnabled: Boolean
        get() = user.firstName.isNotBlank() && user.lastName.isNotBlank() &&
            !isSaving
}
