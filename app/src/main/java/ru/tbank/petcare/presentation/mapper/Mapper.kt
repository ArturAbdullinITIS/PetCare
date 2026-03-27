package ru.tbank.petcare.presentation.mapper

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.model.IconStatus
import ru.tbank.petcare.presentation.model.PetIconStatusUIModel
import ru.tbank.petcare.presentation.model.QuickActionType
import ru.tbank.petcare.presentation.model.QuickActionUIModel
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
                iconSize = 8.dp
            )
        }
        IconStatus.SPARKLES -> {
            PetIconStatusUIModel(
                imageVector = SparklesIconStatus,
                backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                iconSize = 10.dp
            )
        }
        IconStatus.STAR -> {
            PetIconStatusUIModel(
                imageVector = StarIconStatus,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                iconSize = 10.dp
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

