package ru.tbank.petcare.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.usecase.pets.IsOnlineUseCase
import ru.tbank.petcare.domain.usecase.users.GetCurrentUserIdUseCase
import javax.inject.Inject

@HiltViewModel
class NavHostViewModel @Inject constructor(
    private val isOnlineUseCase: IsOnlineUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = isOnlineUseCase()

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUserIdUseCase().collect { result ->
                _currentUserId.update {
                    if (result.isSuccess) result.data else null
                }
            }
        }
    }
}
