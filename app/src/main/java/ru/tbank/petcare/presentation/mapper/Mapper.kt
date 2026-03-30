package ru.tbank.petcare.presentation.mapper

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.model.Gender
import ru.tbank.petcare.domain.model.IconStatus
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.presentation.model.PetForm
import ru.tbank.petcare.presentation.model.PetIconStatusUIModel
import ru.tbank.petcare.presentation.model.QuickActionType
import ru.tbank.petcare.presentation.model.QuickActionUIModel
import ru.tbank.petcare.presentation.navigation.NavigationBarRoute
import ru.tbank.petcare.presentation.navigation.Route
import ru.tbank.petcare.presentation.ui.theme.GroomingQuickActionIcon
import ru.tbank.petcare.presentation.ui.theme.HeartIconStatus
import ru.tbank.petcare.presentation.ui.theme.SparklesIconStatus
import ru.tbank.petcare.presentation.ui.theme.StarIconStatus
import ru.tbank.petcare.presentation.ui.theme.VetQuickActionIcon
import ru.tbank.petcare.presentation.ui.theme.WalkQuickActionIcon
import javax.inject.Inject

@Composable
fun getIconStatusUI(iconStatus: IconStatus): PetIconStatusUIModel? {
    return when (iconStatus) {
        IconStatus.NONE -> null
        IconStatus.HEART -> {
            PetIconStatusUIModel(
                imageVector = HeartIconStatus,
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        IconStatus.SPARKLES -> {
            PetIconStatusUIModel(
                imageVector = SparklesIconStatus,
                backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        }
        IconStatus.STAR -> {
            PetIconStatusUIModel(
                imageVector = StarIconStatus,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}


@Composable
fun getQuickActionUI(type: QuickActionType): QuickActionUIModel {
    return when (type) {
        QuickActionType.WALK -> {
            QuickActionUIModel(
                title = R.string.walk_quick_action_title,
                icon = WalkQuickActionIcon,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        QuickActionType.VET -> {
            QuickActionUIModel(
                title = R.string.vet_quick_action_title,
                icon = VetQuickActionIcon,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        QuickActionType.GROOMING -> {
            QuickActionUIModel(
                title = R.string.grooming_quick_action_title,
                icon = GroomingQuickActionIcon,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}


@Composable
fun Gender.toIndex(): Int {
    return when(this) {
        Gender.MALE -> 0
        Gender.FEMALE -> 1
        Gender.UNKNOWN -> 2
    }
}

fun PetForm.toDomain(): Pet {
    return Pet(
        id = "",
        ownerId = "",
        gameScore = 0,
        name = name,
        breed = breed,
        gender = gender,
        isPublic = isPublic,
        note = note,
        weight = weight.toDouble(),
        dateOfBirth = dateOfBirth,
        iconStatus = iconStatus,
        photoUrl = photoUrl
    )
}


