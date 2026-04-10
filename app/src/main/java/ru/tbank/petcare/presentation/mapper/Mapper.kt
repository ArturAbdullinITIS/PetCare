package ru.tbank.petcare.presentation.mapper

import android.R.attr.name
import android.R.attr.subtitle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.model.Activity
import ru.tbank.petcare.domain.model.ActivityDetails
import ru.tbank.petcare.domain.model.ActivityType
import ru.tbank.petcare.domain.model.Gender
import ru.tbank.petcare.domain.model.IconStatus
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.model.User
import ru.tbank.petcare.presentation.model.PetCardUIModel
import ru.tbank.petcare.presentation.model.PetForm
import ru.tbank.petcare.presentation.model.PetIconStatusUIModel
import ru.tbank.petcare.presentation.model.PublicPetCardUIModel
import ru.tbank.petcare.presentation.model.QuickActionType
import ru.tbank.petcare.presentation.model.QuickActionUIModel
import ru.tbank.petcare.presentation.screen.createActivity.ActivityFormState
import ru.tbank.petcare.presentation.screen.createActivity.CreateActivityState
import ru.tbank.petcare.presentation.model.UserForm
import ru.tbank.petcare.presentation.ui.theme.GroomingQuickActionIcon
import ru.tbank.petcare.presentation.ui.theme.HeartIconStatus
import ru.tbank.petcare.presentation.ui.theme.SparklesIconStatus
import ru.tbank.petcare.presentation.ui.theme.StarIconStatus
import ru.tbank.petcare.presentation.ui.theme.VetQuickActionIcon
import ru.tbank.petcare.presentation.ui.theme.WalkQuickActionIcon
import ru.tbank.petcare.utils.DateFormatter

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
    return when (this) {
        Gender.MALE -> 0
        Gender.FEMALE -> 1
        Gender.UNKNOWN -> 2
    }
}

fun PetForm.toDomain(): Pet {
    return Pet(
        id = id,
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

fun Pet.toForm(): PetForm {
    val gameScoreText = "$gameScore pts"
    return PetForm(
        id = id,
        name = name,
        breed = breed,
        gender = gender,
        isPublic = isPublic,
        note = note,
        weight = weight.toString(),
        dateOfBirth = dateOfBirth,
        dateOfBirthText = DateFormatter.formatDob(dateOfBirth),
        iconStatus = iconStatus,
        photoUrl = photoUrl,
        gameScore = gameScore,
        gameScoreText = gameScoreText,
        ownerId = ownerId
    )
}

fun Pet.toPetCardUIModel(): PetCardUIModel {
    val age = DateFormatter.formatAgeYearsMonths(dateOfBirth)
    val subtitle = listOf(breed, age).filter { it.isNotBlank() }.joinToString(" • ")

    return PetCardUIModel(
        id = id,
        name = name,
        photoUrl = photoUrl,
        iconStatus = iconStatus,
        subtitle = subtitle
    )
}


fun ActivityFormState.toIndex(): Int {
    return when (this) {
        is ActivityFormState.Walk -> 0
        is ActivityFormState.Grooming -> 1
        is ActivityFormState.Vet -> 2
    }
}

fun CreateActivityState.toDomain(): Activity {
    return Activity(
        id = "",
        activityType = when (this.activityType) {
            is ActivityFormState.Walk -> ActivityType.WALK
            is ActivityFormState.Grooming -> ActivityType.GROOMING
            is ActivityFormState.Vet -> ActivityType.VET
        },
        activityDate = this.activityDate,
        notes = this.activityNotes,
        details = when (this.activityType) {
            is ActivityFormState.Grooming -> ActivityDetails.Grooming(procedureType = this.activityType.form.procedureType, durationMinutes = this.activityType.form.durationMinutes, groomingCost = this.activityType.form.groomingCost )
            is ActivityFormState.Vet -> ActivityDetails.Vet(vetCost = this.activityType.form.vetCost, procedureType = this.activityType.form.procedureType)
            is ActivityFormState.Walk -> ActivityDetails.Walk(goalKm = this.activityType.form.goalKm, actualKm = this.activityType.form.actualKm )
        }
    )
}
fun Pet.toPublicPetCardUIModel(isMine: Boolean): PublicPetCardUIModel {
    val gameScoreField = "$gameScore pts"
    val genderFormatted = gender.name.lowercase().replaceFirstChar { ch ->
        if (ch.isLowerCase()) ch.titlecase() else ch.toString()
    }
    val noteFormatted = note.ifBlank { "No Info" }
    return PublicPetCardUIModel(
        id = id,
        name = name,
        photoUrl = photoUrl,
        note = noteFormatted,
        gameScore = gameScoreField,
        gender = genderFormatted,
        breed = breed,
        isMine = isMine
    )
}

fun UserForm.toDomain(): User {
    return User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        photoUrl = photoUrl
    )
}

fun User.toUserForm(): UserForm {
    return UserForm(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        photoUrl = this.photoUrl
    )
}
