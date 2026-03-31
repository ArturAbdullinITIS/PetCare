package ru.tbank.petcare.presentation.screen.mypets

import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.model.Tip

data class MyPetsState(
    val tips: List<Tip> = emptyList(),
    val pets: List<Pet> = emptyList(),
    val isPetsLoading: Boolean = false,
    val isTipsLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentTipIndex: Int = 0
) {
    val currentTip: Tip?
        get() = tips.getOrNull(currentTipIndex)
}
