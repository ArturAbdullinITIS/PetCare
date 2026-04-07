package ru.tbank.petcare.presentation.model

data class VetActivityForm(
    val procedureType: VetProcedureType = VetProcedureType.CHECKUP,
    val vetCost: String = "",
)

enum class VetProcedureType(val value: String) {
    CHECKUP("Checkup"),
    VACCINATION("Vaccination"),
    SURGERY("Surgery"),
    DENTAL("Dental"),
    OTHER("Other")
}
