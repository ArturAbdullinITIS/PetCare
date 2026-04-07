package ru.tbank.petcare.presentation.screen.createActivity

import ru.tbank.petcare.presentation.model.GroomingActivityForm
import ru.tbank.petcare.presentation.model.PetCardUIModel
import ru.tbank.petcare.presentation.model.VetActivityForm
import ru.tbank.petcare.presentation.model.WalkActivityForm
import java.util.Date

@Suppress("MaximumLineLength", "MaxLineLength")
data class CreateActivityState(
    val selectedPetId: String = "",
    val pets: List<PetCardUIModel> = emptyList(),
    val activityDate: Date? = null,
    val activityDateText: String = "",
    val activityNotes: String = "",
    val activityType: ActivityFormState = ActivityFormState.Walk(),
    val isReminder: Boolean = false,
    val isCreating: Boolean = false,
) {
    val isButtonEnabled: Boolean
        get() = selectedPetId.isNotBlank() && activityDate != null && !isCreating && when (activityType) {
            is ActivityFormState.Walk -> activityType.form.goalKm.isNotBlank() && activityType.form.actualKm.isNotBlank()
            is ActivityFormState.Grooming -> activityType.form.durationMinutes.isNotBlank() && activityType.form.groomingCost.isNotBlank()
            is ActivityFormState.Vet -> activityType.form.vetCost.isNotBlank()
        }
}

sealed interface ActivityFormState {
    data class Walk(val form: WalkActivityForm = WalkActivityForm()) : ActivityFormState
    data class Grooming(val form: GroomingActivityForm = GroomingActivityForm()) : ActivityFormState
    data class Vet(val form: VetActivityForm = VetActivityForm()) : ActivityFormState

    companion object {
        fun list(): List<ActivityFormState> {
            return listOf(Walk(), Grooming(), Vet())
        }
    }
}
