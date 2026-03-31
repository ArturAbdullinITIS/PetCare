package ru.tbank.petcare.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest

import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val applicationContext: Context
) : AuthRepository {
    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(activityContext: Context): Result<Unit> {
        val credentialManager = CredentialManager.create(activityContext)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(applicationContext.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(
                context = activityContext,
                request = request
            )

            val credential = result.credential
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                firebaseAuth.signInWithCredential(authCredential).await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Unexpected custom credential type"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

        }



    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
