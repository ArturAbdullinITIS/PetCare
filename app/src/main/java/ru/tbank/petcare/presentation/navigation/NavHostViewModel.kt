package ru.tbank.petcare.presentation.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import ru.tbank.petcare.domain.usecase.pets.IsOnlineUseCase
import javax.inject.Inject

@HiltViewModel
class NavHostViewModel @Inject constructor(
    private val isOnlineUseCase: IsOnlineUseCase
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = isOnlineUseCase()
}
