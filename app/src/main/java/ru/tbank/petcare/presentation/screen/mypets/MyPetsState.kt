package ru.tbank.petcare.presentation.screen.mypets

import ru.tbank.petcare.domain.model.Tip
import ru.tbank.petcare.presentation.model.PetCardUIModel

data class MyPetsState(
    val tips: List<Tip> = emptyList(),
    val pets: List<PetCardUIModel> = emptyList(),
    val isPetsLoading: Boolean = false,
    val isTipsLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentTipIndex: Int = 0,
    val isRefreshing: Boolean = false
) {
    val currentTip: Tip?
        get() = tips.getOrNull(currentTipIndex)
}
