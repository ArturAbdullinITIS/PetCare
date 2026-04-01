package ru.tbank.petcare.domain.model

data class ValidationResult<T>(
    val isSuccess: Boolean = false,
    val error: ErrorType? = null,
    val data: T? = null,
)

sealed class ErrorType(open val message: String = "") {
    data class NetworkError(override val message: String = "") : ErrorType(message)
    data class AuthenticationError(override val message: String = "") : ErrorType(message)
    data class CommonError(override val message: String = "") : ErrorType(message)
    data class NotFoundError(override val message: String = "") : ErrorType(message)
}
