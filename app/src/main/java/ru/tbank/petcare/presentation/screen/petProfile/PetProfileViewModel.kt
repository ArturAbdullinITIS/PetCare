package ru.tbank.petcare.presentation.screen.petProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.domain.usecase.GetPetUseCase
import ru.tbank.petcare.presentation.model.PetForm
import javax.inject.Inject

@HiltViewModel(assistedFactory = PetProfileViewModel.Factory::class)
class PetProfileViewModel @AssistedInject constructor(
    private val getPetUseCase: GetPetUseCase,
    @Assisted("pet_id") private val petId: String
): ViewModel(){


    private val _state = MutableStateFlow(PetProfileState())
    val state = _state.asStateFlow()

    init {
        loadPet()
    }

    private fun loadPet() {
        viewModelScope.launch {
            getPetUseCase(petId).collect { pet ->
                _state.update { state ->
                    state.copy(
                        petProfileUIModel = PetForm(
                            name = pet.name,
                            breed = pet.breed,
                            gender = pet.gender,
                            isPublic = pet.isPublic,
                            note = pet.note,
                            weight = pet.weight.toString(),
                            dateOfBirth = pet.dateOfBirth,
                            iconStatus = pet.iconStatus,
                            photoUrl = pet.photoUrl
                        )
                    )
                }
            }
        }
    }





    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("pet_id") petId: String
        ): PetProfileViewModel
    }
}

data class PetProfileState(
    val petProfileUIModel: PetForm = PetForm()
)