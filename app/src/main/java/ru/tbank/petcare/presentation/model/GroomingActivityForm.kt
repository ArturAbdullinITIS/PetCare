package ru.tbank.petcare.presentation.model

data class GroomingActivityForm(
    val procedureType: GroomingProcedureType = GroomingProcedureType.BATH,
    val groomingCost: String = "",
    val durationMinutes: String = ""
)

enum class GroomingProcedureType(val value: String) {
    BATH("Bath"),
    CLAWS("Claws"),
    HAIRCUT("Haircut"),
    BRUSH("Brush"),
    FULL_SERVICE("Full service")
}
