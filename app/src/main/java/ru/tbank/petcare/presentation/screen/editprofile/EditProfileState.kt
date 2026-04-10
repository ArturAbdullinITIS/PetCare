package ru.tbank.petcare.presentation.screen.editprofile

import android.net.Uri
import ru.tbank.petcare.presentation.model.UserForm


data class EditProfileState(
    val user: UserForm = UserForm(),
    val selectedPhotoUri: Uri? = null,
    val isEditing: Boolean = false
) {
    val isEnabled: Boolean
        get() = user.firstName.isNotBlank() && user.lastName.isNotBlank() && !isEditing
}