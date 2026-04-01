package ru.tbank.petcare.utils

import ru.tbank.petcare.R
import ru.tbank.petcare.domain.model.ValidationError

class ErrorParser(private val resourceProvider: ResourceProvider) {
    fun parse(error: ValidationError): String {
        return when (error) {
            ValidationError.EMAIL_BLANK ->
                resourceProvider.getString(R.string.error_email_blank)
            ValidationError.EMAIL_INVALID ->
                resourceProvider.getString(R.string.error_email_invalid)
            ValidationError.PASSWORD_SHORT ->
                resourceProvider.getString(R.string.error_password_short)

            ValidationError.PASSWORDS_DO_NOT_MATCH ->
                resourceProvider.getString(R.string.error_passwords_do_not_match)

            ValidationError.NAME_SHORT -> TODO()
            ValidationError.WEIGHT_INVALID -> TODO()
            ValidationError.DATE_OF_BIRTH_INVALID -> TODO()
        }
    }
}