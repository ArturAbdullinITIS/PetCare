package ru.tbank.petcare.data.repository

import android.annotation.SuppressLint
import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import ru.tbank.petcare.domain.model.ErrorType
import ru.tbank.petcare.domain.model.Language
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.AuthRepository
import ru.tbank.petcare.domain.repository.SettingsRepository
import ru.tbank.petcare.domain.repository.TranslationRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager,
    private val getCredentialRequest: GetCredentialRequest,
    private val firestore: FirebaseFirestore,
    private val translationRepository: TranslationRepository,
    private val settingsRepository: SettingsRepository
) : AuthRepository {

    companion object {
        private const val GOOGLE_ID_ERROR = "Google idToken is blank"
        private const val UNEXPECTED_CREDENTIAL_ERROR = "Unexpected credential type"
        private const val USERS_COLLECTION = "users"
        private const val KEY_EMAIL = "email"
        private const val KEY_FIRST_NAME = "first_name"
        private const val KEY_LAST_NAME = "last_name"
        private const val KEY_PHOTO_URL = "photo_url"
    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): ValidationResult<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            ValidationResult(isSuccess = true, data = Unit)
        } catch (e: FirebaseAuthException) {
            val errorMessage = translateErrorMessage(e.message.orEmpty())
            when (e) {
                is FirebaseAuthWeakPasswordException ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthValidation(errorMessage))

                is FirebaseAuthInvalidCredentialsException ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthCredentials(errorMessage))

                is FirebaseAuthUserCollisionException ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthConflict(errorMessage))

                else ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthUnknown(errorMessage))
            }
        }
    }

    @Suppress("CyclomaticComplexMethod", "NestedBlockDepth")
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
                val authResult = firebaseAuth.signInWithCredential(authCredential).await()

                val user = authResult.user
                if (user != null) {
                    val userDocRef = firestore.collection(USERS_COLLECTION).document(user.uid)
                    val document = userDocRef.get().await()

                    if (!document.exists()) {
                        val newUserDto = hashMapOf(
                            KEY_EMAIL to (user.email ?: ""),
                            KEY_FIRST_NAME to (user.displayName ?: ""),
                            KEY_LAST_NAME to "",
                            KEY_PHOTO_URL to (user.photoUrl?.toString() ?: "")
                        )
                        userDocRef.set(newUserDto).await()
                    }
                }

                ValidationResult(isSuccess = true, data = Unit)
            } else {
                val errorMessage = translateErrorMessage(UNEXPECTED_CREDENTIAL_ERROR)
                ValidationResult(
                    isSuccess = false,
                    error = ErrorType.AuthUnknown(errorMessage)
                )
            }
        } catch (e: GetCredentialCancellationException) {
            val errorMessage = translateErrorMessage(e.message.orEmpty())
            ValidationResult(isSuccess = false, error = ErrorType.AuthCancelled(errorMessage))
        } catch (e: GetCredentialException) {
            val errorMessage = translateErrorMessage(e.message.orEmpty())
            ValidationResult(isSuccess = false, error = ErrorType.AuthUnknown(errorMessage))
        } catch (e: FirebaseAuthException) {
            val errorMessage = translateErrorMessage(e.message.orEmpty())
            when (e) {
                is FirebaseAuthInvalidCredentialsException ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthCredentials(errorMessage))

                is FirebaseAuthUserCollisionException ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthConflict(errorMessage))

                else ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthUnknown(errorMessage))
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
            val errorMessage = translateErrorMessage(e.message.orEmpty())
            when (e) {
                is FirebaseAuthInvalidCredentialsException ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthCredentials(errorMessage))

                is FirebaseAuthInvalidUserException ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthCredentials(errorMessage))

                else ->
                    ValidationResult(isSuccess = false, error = ErrorType.AuthUnknown(errorMessage))
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override suspend fun signOut(): ValidationResult<Unit> {
        return try {
            firebaseAuth.signOut()
            val clearRequest = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(clearRequest)
            ValidationResult(
                isSuccess = true,
                data = Unit
            )
        } catch (e: ClearCredentialException) {
            val errorMessage = translateErrorMessage(e.message.orEmpty())
            ValidationResult(
                isSuccess = false,
                error = ErrorType.AuthCredentials(errorMessage)
            )
        }
    }

    override suspend fun getCurrentUserId(): Flow<ValidationResult<String>> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val currentUser = auth.currentUser
            val result = if (currentUser != null) {
                ValidationResult(
                    isSuccess = true,
                    data = currentUser.uid
                )
            } else {
                ValidationResult(
                    isSuccess = false,
                    error = ErrorType.AuthValidation()
                )
            }
            trySend(result)
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    private suspend fun translateErrorMessage(errorMessage: String): String {
        val currentLanguage = settingsRepository.getSettings().first().language

        return when {
            errorMessage.isBlank() -> errorMessage
            currentLanguage == Language.ENGLISH -> errorMessage
            else -> translationRepository.translate(errorMessage, Language.ENGLISH, currentLanguage)
                .firstOrNull() ?: errorMessage
        }
    }
}
