package ru.tbank.petcare.presentation.screen.publicPetProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.usecase.pets.GetPetInfoUseCase
import ru.tbank.petcare.domain.usecase.pets.GetPetUseCase
import ru.tbank.petcare.domain.usecase.users.GetUserNameUseCase
import ru.tbank.petcare.presentation.mapper.toForm
import ru.tbank.petcare.utils.ErrorParser
import ru.tbank.petcare.utils.ResourceProvider

@HiltViewModel(assistedFactory = PublicPetProfileViewModel.Factory::class)
class PublicPetProfileViewModel @AssistedInject constructor(
    private val getPetUseCase: GetPetUseCase,
    private val getPetInfoUseCase: GetPetInfoUseCase,
    private val errorParser: ErrorParser,
    @Assisted(PET_ID) private val petId: String,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    companion object {
        private const val PET_ID = "pet_id"
    }

    private val _state = MutableStateFlow(PublicPetProfileState())
    val state = _state.asStateFlow()

    init {
        loadPet()
    }

    private fun loadPet() {
        viewModelScope.launch {
            var cachedOwnerId: String? = null
            var cachedOwnerName: String? = null

            getPetUseCase(petId).collect { pet ->
                val form = pet.toForm()

                _state.update { st ->
                    st.copy(
                        petProfileUIModel = form.copy(ownerName = cachedOwnerName.orEmpty())
                    )
                }

                val ownerId = pet.ownerId
                if (ownerId.isBlank() || ownerId == cachedOwnerId) return@collect

                cachedOwnerId = ownerId
                val nameResult = getUserNameUseCase(ownerId)
                cachedOwnerName = nameResult.data.orEmpty()

                _state.update { st ->
                    st.copy(
                        petProfileUIModel = st.petProfileUIModel.copy(
                            ownerName = cachedOwnerName.ifBlank {
                                resourceProvider.getString(
                                    R.string.unknown_owner
                                )
                            }
                        )
                    )
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(PET_ID) petId: String
        ): PublicPetProfileViewModel
    }

    fun processCommand(command: PublicPetProfileCommand) {
        when (command) {
            is PublicPetProfileCommand.ShowPetInfo -> {
                viewModelScope.launch {
                    _state.update { state ->
                        state.copy(
                            isInfoLoading = true,
                            errorMessage = null
                        )
                    }
                    val petInfo = getPetInfoUseCase(command.breed)

                    if (petInfo.isSuccess && petInfo.data != null) {
                        _state.update { state ->
                            state.copy(
                                petInfoUIModel = petInfo.data,
                                isInfoLoading = false
                            )
                        }
                    } else {
                        val errorMessage = errorParser.getErrorMessage(petInfo.error)
                        _state.update { state ->
                            state.copy(
                                isInfoLoading = false,
                                errorMessage = errorMessage
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed interface PublicPetProfileCommand {
    data class ShowPetInfo(val breed: String) : PublicPetProfileCommand
}
