package ru.tbank.petcare.presentation.screen.addpet

import ru.tbank.petcare.presentation.model.PetForm

data class AddPetState(
    val petUIModel: PetForm = PetForm(),
) {
    val isButtonEnabled: Boolean
        get() = petUIModel.name.isNotBlank() && petUIModel.breed.isNotBlank() && petUIModel.weight.isNotBlank() &&
            petUIModel.dateOfBirth != 0L && !petUIModel.weight.endsWith(".")
}
