package ru.tbank.petcare.data.mapper

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import ru.tbank.petcare.data.local.PetDbModel
import ru.tbank.petcare.data.remote.firebase.ActivityDto
import ru.tbank.petcare.data.remote.firebase.PetDto
import ru.tbank.petcare.data.remote.firebase.TipDto
import ru.tbank.petcare.data.remote.firebase.UserDto
import ru.tbank.petcare.data.remote.network.animals.AnimalsResponseDto
import ru.tbank.petcare.domain.model.Activity
import ru.tbank.petcare.domain.model.ActivityDetails
import ru.tbank.petcare.domain.model.Gender
import ru.tbank.petcare.domain.model.IconStatus
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.model.PetInfo
import ru.tbank.petcare.domain.model.Tip
import ru.tbank.petcare.domain.model.User
import java.util.Date

fun PetDto.toDomain(): Pet {
    return Pet(
        id = id,
        name = name,
        breed = breed,
        gender = Gender.getGenderFromValue(gender),
        isPublic = isPublic,
        note = note,
        gameScore = gameScore,
        ownerId = ownerId,
        weight = weight,
        dateOfBirth = dateOfBirth?.toDate(),
        iconStatus = IconStatus.getIconStatusFromValue(iconStatus),
        photoUrl = photoUrl
    )
}

fun Pet.toDto(): PetDto {
    return PetDto(
        name = name,
        gender = gender.name,
        breed = breed,
        dateOfBirth = dateOfBirth?.let { Timestamp(it) },
        gameScore = gameScore,
        iconStatus = iconStatus.name,
        isPublic = isPublic,
        note = note,
        ownerId = ownerId,
        photoUrl = photoUrl,
        weight = weight
    )
}

fun TipDto.toDomain(): Tip {
    return Tip(
        id = id,
        text = text
    )
}

fun AnimalsResponseDto.toEntities(): List<PetInfo> {
    return this.map { animalDtoItem ->
        PetInfo(
            breedName = animalDtoItem.name,
            diet = animalDtoItem.characteristics.diet,
            group = animalDtoItem.characteristics.group,
            lifespan = animalDtoItem.characteristics.lifespan,
            skinType = animalDtoItem.characteristics.skinType,
            slogan = animalDtoItem.characteristics.slogan,
            weight = animalDtoItem.characteristics.weight,
            locations = animalDtoItem.locations
        )
    }
}

fun Activity.toDto(): ActivityDto {
    return ActivityDto(
        id = id,
        isReminder = isReminder,
        notes = notes,
        date = activityDate?.let { Timestamp(it) },
        type = activityType.name,
        details = when (val details = details) {
            is ActivityDetails.Walk -> details.toMap()
            is ActivityDetails.Grooming -> details.toMap()
            is ActivityDetails.Vet -> details.toMap()
            else -> emptyMap()
        }
    )
}

fun ActivityDetails.Walk.toMap(): Map<String, Any> {
    return mapOf(
        "goal" to goalKm.toDouble(),
        "actual" to actualKm.toDouble()
    )
}

fun ActivityDetails.Grooming.toMap(): Map<String, Any> {
    return mapOf(
        "procedure_type" to procedureType.name,
        "duration_minutes" to durationMinutes.toInt(),
        "cost" to groomingCost.toDouble()
    )
}

fun ActivityDetails.Vet.toMap(): Map<String, Any> {
    return mapOf(
        "procedure_type" to procedureType.name,
        "cost" to vetCost.toDouble()
    )
}

fun UserDto.toDomain(): User {
    return User(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        photoUrl = photoUrl
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        email = email,
        firstName = firstName,
        lastName = lastName,
        photoUrl = photoUrl
    )
}

fun Pet.toDbModel(): PetDbModel {
    return PetDbModel(
        id = id,
        name = name,
        gender = gender.name,
        breed = breed,
        dateOfBirthMillis = dateOfBirth?.time,
        gameScore = gameScore,
        iconStatus = iconStatus.name,
        isPublic = isPublic,
        note = note,
        ownerId = ownerId,
        photoUrl = photoUrl,
        weight = weight
    )
}

fun PetDbModel.toDomain(): Pet = Pet(
    id = id,
    name = name,
    breed = breed,
    gender = Gender.getGenderFromValue(gender),
    isPublic = isPublic,
    note = note,
    gameScore = gameScore,
    ownerId = ownerId,
    weight = weight,
    dateOfBirth = dateOfBirthMillis?.let { Date(it) },
    iconStatus = IconStatus.getIconStatusFromValue(iconStatus),
    photoUrl = photoUrl
)
