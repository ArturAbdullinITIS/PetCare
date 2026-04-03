package ru.tbank.petcare.data.mapper

import com.google.firebase.Timestamp
import ru.tbank.petcare.data.remote.firebase.PetDto
import ru.tbank.petcare.data.remote.firebase.TipDto
import ru.tbank.petcare.data.remote.network.animals.AnimalsResponseDto
import ru.tbank.petcare.domain.model.Gender
import ru.tbank.petcare.domain.model.IconStatus
import ru.tbank.petcare.domain.model.Pet
import ru.tbank.petcare.domain.model.PetInfo
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
