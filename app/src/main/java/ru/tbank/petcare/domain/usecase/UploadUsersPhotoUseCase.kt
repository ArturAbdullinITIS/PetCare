package ru.tbank.petcare.domain.usecase

import android.net.Uri
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.UsersRepository
import javax.inject.Inject

class UploadUsersPhotoUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(uri: Uri): ValidationResult<String> {
        return usersRepository.uploadUsersPhoto(uri)
    }
}
