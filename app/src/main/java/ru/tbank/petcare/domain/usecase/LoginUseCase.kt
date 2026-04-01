package ru.tbank.petcare.domain.usecase

import com.google.firebase.auth.FirebaseUser
import ru.tbank.petcare.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.signInWithEmailAndPassword(email, password)
    }
}