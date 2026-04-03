package ru.tbank.petcare.presentation.screen.editpet

import android.net.Uri
import ru.tbank.petcare.presentation.model.PetForm

data class EditPetState(
    val petUIModel: PetForm = PetForm(),
    val selectedPhotoUri: Uri? = null,
    val isEditing: Boolean = false,
    val showDeleteDialog: Boolean = false,
) {
    val isButtonEnabled: Boolean
        get() = petUIModel.name.isNotBlank() && petUIModel.breed.isNotBlank() && petUIModel.weight.isNotBlank() &&
            petUIModel.dateOfBirth?.time != 0L && !petUIModel.weight.endsWith(".") && !isEditing
}
