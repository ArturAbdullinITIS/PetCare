package ru.tbank.petcare.domain.repository

import android.content.Context


interface AuthRepository {

    suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Unit>

    suspend fun signInWithGoogle(activityContext: Context): Result<Unit>

    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Unit>
}

