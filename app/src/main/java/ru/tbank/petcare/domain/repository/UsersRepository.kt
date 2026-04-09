package ru.tbank.petcare.domain.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.tbank.petcare.domain.model.User
import ru.tbank.petcare.domain.model.ValidationResult

interface UsersRepository {
    suspend fun createUserProfile(user: User): ValidationResult<Unit>
    suspend fun uploadUsersPhoto(uri: Uri): ValidationResult<String>
    suspend fun getUserNameById(userId: String): ValidationResult<String>
    suspend fun getCurrentUser(): Flow<User>
    suspend fun editUser(user: User): ValidationResult<Unit>
}
