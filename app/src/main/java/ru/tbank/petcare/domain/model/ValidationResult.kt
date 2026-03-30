package ru.tbank.petcare.domain.model


data class ValidationResult(
    val isSuccess: Boolean = false,
    val error: ValidationError? = null
)



enum class ValidationError {
    NAME_SHORT,
    WEIGHT_INVALID,
    DATE_OF_BIRTH_INVALID,
}