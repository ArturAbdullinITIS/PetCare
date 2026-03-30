package ru.tbank.petcare.data.mapper

import ru.tbank.petcare.data.remote.firebase.PetDto
import ru.tbank.petcare.data.remote.firebase.TipDto
import ru.tbank.petcare.domain.model.Gender
import ru.tbank.petcare.domain.model.IconStatus
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.model.Tip


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
        dateOfBirth = dateOfBirth,
        iconStatus = IconStatus.getIconStatusFromValue(iconStatus),
        photoUrl = photoUrl
    )
}

fun Pet.toDto(): PetDto {
    return PetDto(
        name = name,
        gender = gender.name,
        breed = breed,
        dateOfBirth = dateOfBirth,
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