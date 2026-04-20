package ru.tbank.petcare.presentation.model

import androidx.annotation.StringRes

data class OnboardingPageUIModel(
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
    @StringRes val descriptionRes: Int,
    @StringRes val buttonTextRes: Int
)
