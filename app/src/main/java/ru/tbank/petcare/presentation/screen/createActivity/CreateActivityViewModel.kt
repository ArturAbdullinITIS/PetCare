package ru.tbank.petcare.presentation.screen.createActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.usecase.CreateActivityUseCase
import ru.tbank.petcare.domain.usecase.GetAllPetsUseCase
import ru.tbank.petcare.presentation.mapper.toDomain
import ru.tbank.petcare.presentation.mapper.toPetCardUiModel
import ru.tbank.petcare.presentation.model.GroomingProcedureType
import ru.tbank.petcare.presentation.model.VetProcedureType
import ru.tbank.petcare.utils.DateFormatter
import ru.tbank.petcare.utils.ResourceProvider
import java.util.Date

@HiltViewModel(assistedFactory = CreateActivityViewModel.Factory::class)
class CreateActivityViewModel @AssistedInject constructor(
    private val getAllPetsUseCase: GetAllPetsUseCase,
    private val createActivityUseCase: CreateActivityUseCase,
    private val resourceProvider: ResourceProvider,
    @Assisted("petId") private val initialPetId: String?,
    @Assisted("type") private val initialType: String?
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("petId") petId: String?,
            @Assisted("type") type: String?
        ): CreateActivityViewModel
    }

    private val _state = MutableStateFlow(
        CreateActivityState(
            selectedPetId = initialPetId ?: "",
            activityType = getActivityFormState(initialType)
        )
    )
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<CreateActivityEvent>()
    val events = _events.asSharedFlow()

    init {
        loadPet()
    }

    private fun getActivityFormState(type: String?): ActivityFormState {
        return when (type?.lowercase()) {
            "walk" -> ActivityFormState.Walk()
            "grooming" -> ActivityFormState.Grooming()
            "vet" -> ActivityFormState.Vet()
            else -> ActivityFormState.Walk()
        }
    }

    private fun loadPet() {
        viewModelScope.launch {
            getAllPetsUseCase().collect { pets ->
                val uiPet = pets.map { it.toPetCardUiModel() }
                _state.update { state ->
                    state.copy(
                        pets = uiPet
                    )
                }
            }
        }
    }

    fun processCommand(command: CreateActivityCommand) {
        when (command) {
            is CreateActivityCommand.SelectPet -> setPet(command.petId)
            is CreateActivityCommand.ChangeActivityType -> changeActivityType(command.activityType)
            is CreateActivityCommand.InputActivityDate -> setDate(command.date)
            is CreateActivityCommand.InputActualKm -> setActualKm(command.actualKm)
            is CreateActivityCommand.InputGoalKm -> setGoalKm(command.goalKm)
            is CreateActivityCommand.InputGroomingCost -> setGroomingCost(command.cost)
            is CreateActivityCommand.InputGroomingDuration -> setGroomingDuration(command.duration)
            is CreateActivityCommand.InputVetCost -> setVetCost(command.cost)
            is CreateActivityCommand.InputVetProcedure -> setVetProcedure(command.procedureType)
            is CreateActivityCommand.InputNotes -> setNotes(command.activityNotes)
            is CreateActivityCommand.InputGroomingProcedure -> setGroomingProcedure(command.procedureType)
            CreateActivityCommand.IsReminder -> toggleIsReminder()
            CreateActivityCommand.SaveActivity -> handleCreateActivity()
        }
    }

    private fun handleCreateActivity() {
        val activity = state.value.toDomain()

        viewModelScope.launch {
            _state.update { state -> state.copy(isCreating = true) }
            try {
                val result = createActivityUseCase(activity)
                if (result.isSuccess) {
                    _events.emit(CreateActivityEvent.Saved)
                    _state.update { currentState ->
                        CreateActivityState(
                            pets = currentState.pets,
                            selectedPetId = initialPetId ?: "",
                            activityType = getActivityFormState(initialType)
                        )
                    }
                } else {
                    _events.emit(
                        CreateActivityEvent.Error(resourceProvider.getString(R.string.could_not_save_activity))
                    )
                }
            } catch (t: Throwable) {
                _events.emit(CreateActivityEvent.Error(resourceProvider.getString(R.string.unknown_error)))
            } finally {
                _state.update { it.copy(isCreating = false) }
            }
        }
    }

    private fun setPet(petId: String) {
        _state.update { state ->
            state.copy(selectedPetId = petId)
        }
    }
    private fun toggleIsReminder() {
        _state.update { state ->
            state.copy(isReminder = !state.isReminder)
        }
    }

    private fun changeActivityType(activityType: ActivityFormState) {
        _state.update { state ->
            state.copy(activityType = activityType)
        }
    }

    private fun setDate(date: Date?) {
        val normalized = date?.let { DateFormatter.normalizeToStartOfDayUtc(it) }
        val text = DateFormatter.formatDob(normalized)
        _state.update { state ->
            state.copy(
                activityDate = date,
                activityDateText = text
            )
        }
    }

    private fun setNotes(notes: String) {
        _state.update { state ->
            state.copy(activityNotes = notes)
        }
    }

    private fun setGoalKm(goalKm: String) {
        _state.update { state ->
            val currentType = state.activityType
            if (currentType is ActivityFormState.Walk) {
                state.copy(activityType = currentType.copy(form = currentType.form.copy(goalKm = goalKm)))
            } else {
                state
            }
        }
    }

    private fun setActualKm(actualKm: String) {
        _state.update { state ->
            val currentType = state.activityType
            if (currentType is ActivityFormState.Walk) {
                state.copy(activityType = currentType.copy(form = currentType.form.copy(actualKm = actualKm)))
            } else {
                state
            }
        }
    }

    private fun setGroomingCost(groomingCost: String) {
        _state.update { state ->
            val currentType = state.activityType
            if (currentType is ActivityFormState.Grooming) {
                state.copy(activityType = currentType.copy(form = currentType.form.copy(groomingCost = groomingCost)))
            } else {
                state
            }
        }
    }

    private fun setGroomingDuration(duration: String) {
        _state.update { state ->
            val currentType = state.activityType
            if (currentType is ActivityFormState.Grooming) {
                state.copy(activityType = currentType.copy(form = currentType.form.copy(durationMinutes = duration)))
            } else {
                state
            }
        }
    }

    private fun setGroomingProcedure(procedureType: GroomingProcedureType) {
        _state.update { state ->
            val currentType = state.activityType
            if (currentType is ActivityFormState.Grooming) {
                state.copy(activityType = currentType.copy(form = currentType.form.copy(procedureType = procedureType)))
            } else {
                state
            }
        }
    }

    private fun setVetCost(vetCost: String) {
        _state.update { state ->
            val currentType = state.activityType
            if (currentType is ActivityFormState.Vet) {
                state.copy(activityType = currentType.copy(form = currentType.form.copy(vetCost = vetCost)))
            } else {
                state
            }
        }
    }

    private fun setVetProcedure(procedureType: VetProcedureType) {
        _state.update { state ->
            val currentType = state.activityType
            if (currentType is ActivityFormState.Vet) {
                state.copy(activityType = currentType.copy(form = currentType.form.copy(procedureType = procedureType)))
            } else {
                state
            }
        }
    }
}

sealed interface CreateActivityCommand {

    data class SelectPet(val petId: String) : CreateActivityCommand
    data class ChangeActivityType(val activityType: ActivityFormState) : CreateActivityCommand
    data class InputActivityDate(val date: Date?) : CreateActivityCommand
    data class InputNotes(val activityNotes: String) : CreateActivityCommand

    // walk
    data class InputGoalKm(val goalKm: String) : CreateActivityCommand
    data class InputActualKm(val actualKm: String) : CreateActivityCommand

    // grooming
    data class InputGroomingCost(val cost: String) : CreateActivityCommand
    data class InputGroomingDuration(val duration: String) : CreateActivityCommand
    data class InputGroomingProcedure(val procedureType: GroomingProcedureType) : CreateActivityCommand

    // vet
    data class InputVetCost(val cost: String) : CreateActivityCommand
    data class InputVetProcedure(val procedureType: VetProcedureType) : CreateActivityCommand
    object IsReminder : CreateActivityCommand
    object SaveActivity : CreateActivityCommand
}
