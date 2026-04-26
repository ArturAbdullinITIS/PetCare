package ru.tbank.petcare.domain.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow
import ru.tbank.petcare.domain.model.ValidationResult

interface AuthRepository {

    val authState: Flow<String?>

    suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): ValidationResult<Unit>

    suspend fun signInWithGoogle(activityContext: Context): ValidationResult<Unit>

    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): ValidationResult<Unit>

    suspend fun signOut(): ValidationResult<Unit>

    suspend fun getCurrentUserId(): ValidationResult<String>
}
