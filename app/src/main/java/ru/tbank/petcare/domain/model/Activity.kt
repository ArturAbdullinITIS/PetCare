package ru.tbank.petcare.domain.model

import ru.tbank.petcare.presentation.model.GroomingProcedureType
import ru.tbank.petcare.presentation.model.VetProcedureType
import java.util.Date

data class Activity(
    val id: String = "",
    val activityType: ActivityType = ActivityType.WALK,
    val activityDate: Date? = null,
    val notes: String = "",
    val details: ActivityDetails = ActivityDetails.Walk(),
    val isReminder: Boolean = false,
)

enum class ActivityType(val value: String) {
    WALK("Walk"),
    GROOMING("Grooming"),
    VET("Vet")
}

sealed interface ActivityDetails {

    data class Walk(
        val goalKm: String = "0",
        val actualKm: String = "0"
    ) : ActivityDetails

    data class Grooming(
        val procedureType: GroomingProcedureType = GroomingProcedureType.FULL_SERVICE,
        val groomingCost: String = "0",
        val durationMinutes: String = "0"
    ) : ActivityDetails

    data class Vet(
        val procedureType: VetProcedureType = VetProcedureType.CHECKUP,
        val vetCost: String = "0"
    ) : ActivityDetails
}
