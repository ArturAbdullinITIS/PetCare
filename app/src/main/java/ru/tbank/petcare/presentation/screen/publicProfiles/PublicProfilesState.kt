package ru.tbank.petcare.presentation.screen.publicProfiles

import ru.tbank.petcare.presentation.model.PublicPetCardUIModel
import ru.tbank.petcare.presentation.model.PublicProfilesSortOption

data class PublicProfilesState(
    val pets: List<PublicPetCardUIModel> = emptyList(),
    val isPetsLoading: Boolean = false,
    val errorMessage: String? = null,
    val sortOption: PublicProfilesSortOption = PublicProfilesSortOption.GAME_SCORE,
) {
    val sortedPets: List<PublicPetCardUIModel>
        get() = when (sortOption) {
            PublicProfilesSortOption.GAME_SCORE -> pets.sortedByDescending { it.gameScore }
            PublicProfilesSortOption.NAME -> pets.sortedBy { it.name }
        }
}
