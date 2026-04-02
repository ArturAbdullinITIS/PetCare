package ru.tbank.petcare.presentation.screen.addpet

import android.net.Uri
import ru.tbank.petcare.presentation.model.PetForm

data class AddPetState(
    val petUIModel: PetForm = PetForm(),
    val selectedPhotoUri: Uri? = null,
    val isUploadingPhoto: Boolean = false,
) {
    val isButtonEnabled: Boolean
        get() = petUIModel.name.isNotBlank() && petUIModel.breed.isNotBlank() && petUIModel.weight.isNotBlank() &&
            petUIModel.dateOfBirth != 0L && !petUIModel.weight.endsWith(".")
}
