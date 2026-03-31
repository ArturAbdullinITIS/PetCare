package ru.tbank.petcare.domain.usecase

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import ru.tbank.petcare.domain.repository.AuthRepository
import javax.inject.Inject


class SignInWithGoogleUseCase @Inject constructor(
    val authRepository: AuthRepository
) {

    suspend operator fun invoke(context: Context): Result<Unit> {
       return authRepository.signInWithGoogle(context)
    }
}