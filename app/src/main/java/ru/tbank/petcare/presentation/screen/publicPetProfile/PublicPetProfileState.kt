package ru.tbank.petcare.presentation.screen.publicPetProfile

import ru.tbank.petcare.domain.model.PetInfo
import ru.tbank.petcare.presentation.model.PetForm

data class PublicPetProfileState(
    val petProfileUIModel: PetForm = PetForm(),
    val petInfoUIModel: PetInfo? = null,
    val errorMessage: String? = null,
    val isInfoLoading: Boolean = false
)
