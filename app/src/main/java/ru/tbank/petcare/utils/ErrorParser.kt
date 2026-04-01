package ru.tbank.petcare.utils

import ru.tbank.petcare.R
import ru.tbank.petcare.domain.model.ErrorType

class ErrorParser(
    private val resourceProvider: ResourceProvider
) {
    fun getErrorMessage(error: ErrorType?): String {
        return when (error) {
            is ErrorType.AuthenticationError -> resourceProvider.getString(
                R.string.authentication_error,
                error.message
            )
            is ErrorType.CommonError -> resourceProvider.getString(
                R.string.common_error,
                error.message
            )
            is ErrorType.NetworkError -> resourceProvider.getString(
                R.string.network_error,
                error.message
            )
            is ErrorType.NotFoundError -> resourceProvider.getString(
                R.string.not_found_error,
                error.message
            )

            null -> resourceProvider.getString(R.string.unknown_error)
        }
    }
}
