package ru.tbank.petcare.domain.usecase.pets

import android.net.Uri
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.PetsRepository
import javax.inject.Inject

class UploadPetPhotoUseCase @Inject constructor(
    private val petsRepository: PetsRepository
) {
    suspend operator fun invoke(uri: Uri): ValidationResult<String> {
        return petsRepository.uploadPetPhoto(uri)
    }
}
