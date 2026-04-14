package ru.tbank.petcare.presentation.screen.allRecentActivities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.util.CoilUtils.result
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.usecase.pets.GetLastActivitiesUseCase

@HiltViewModel(assistedFactory = AllRecentActivitiesViewModel.Factory::class)
class AllRecentActivitiesViewModel @AssistedInject constructor(
    private val getLastActivitiesUseCase: GetLastActivitiesUseCase,
    @Assisted(PET_ID) private val petId: String
) : ViewModel() {
    companion object {
        private const val PET_ID = "pet_id"
    }
    private val _state = MutableStateFlow(AllRecentActivitiesState())
    val state = _state.asStateFlow()

    init {
        loadRecentActivities(10)
    }

    private fun loadRecentActivities(limit: Int) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }
            val result = getLastActivitiesUseCase(petId, limit)
            if (result.isSuccess && result.data != null) {
                _state.update { state ->
                    state.copy(
                        activities = result.data,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } else {
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = result.error?.message
                    )
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(PET_ID) petId: String
        ): AllRecentActivitiesViewModel
    }
}
