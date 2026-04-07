package ru.tbank.petcare.data.repository

import android.content.Context
import android.util.Log.e
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import ru.tbank.petcare.domain.model.ErrorType
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager,
    private val getCredentialRequest: GetCredentialRequest
) : AuthRepository {
    companion object {
        private const val GOOGLE_ID_ERROR = "Google idToken is blank"
        private const val UNEXPECTED_CREDENTIAL_ERROR = "Unexpected credential type"
    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): ValidationResult<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            ValidationResult(isSuccess = true, data = Unit)
        } catch (e: FirebaseAuthException) {
            when (e) {
                is FirebaseAuthWeakPasswordException ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthValidation(e.message.orEmpty()))

                is FirebaseAuthInvalidCredentialsException ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthCredentials(e.message.orEmpty()))

                is FirebaseAuthUserCollisionException ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthConflict(e.message.orEmpty()))

                else ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthUnknown(e.message.orEmpty()))
            }
        }
    }

    override suspend fun signInWithGoogle(activityContext: Context): ValidationResult<Unit> {
        return try {
            val result = credentialManager.getCredential(
                context = activityContext,
                request = getCredentialRequest
            )

            val credential = result.credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken

                if (idToken.isNullOrBlank()) {
                    return ValidationResult(
                        isSuccess = false,
                        error = ErrorType.AuthUnknown(GOOGLE_ID_ERROR)
                    )
                }

                val authCredential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.signInWithCredential(authCredential).await()

                ValidationResult(isSuccess = true, data = Unit)
            } else {
                ValidationResult(
                    isSuccess = false,
                    error = ErrorType.AuthUnknown(UNEXPECTED_CREDENTIAL_ERROR)
                )
            }
        } catch (e: GetCredentialCancellationException) {
            ValidationResult(isSuccess = false, error = ErrorType.AuthCancelled(e.message.orEmpty()))
        } catch (e: GetCredentialException) {
            ValidationResult(isSuccess = false, error = ErrorType.AuthUnknown(e.message.orEmpty()))
        } catch (e: FirebaseAuthException) {
            when (e) {
                is FirebaseAuthInvalidCredentialsException ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthCredentials(e.message.orEmpty()))

                is FirebaseAuthUserCollisionException ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthConflict(e.message.orEmpty()))

                else ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthUnknown(e.message.orEmpty()))
            }
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): ValidationResult<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            ValidationResult(isSuccess = true, data = Unit)
        } catch (e: FirebaseAuthException) {
            when (e) {
                is FirebaseAuthInvalidCredentialsException ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthCredentials(e.message.orEmpty()))

                is FirebaseAuthInvalidUserException ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthCredentials(e.message.orEmpty()))

                else ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthUnknown(e.message.orEmpty()))
            }
        }
    }

    override suspend fun getCurrentUserId(): ValidationResult<String> {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            return ValidationResult(
                isSuccess = true,
                data = currentUser.uid
            )
        } else {
            return ValidationResult(
                isSuccess = false,
                error = ErrorType.AuthValidation()
            )
        }
    }
}
