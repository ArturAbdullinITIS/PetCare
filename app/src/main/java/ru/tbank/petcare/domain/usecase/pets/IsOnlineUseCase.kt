package ru.tbank.petcare.domain.usecase.pets

import kotlinx.coroutines.flow.StateFlow
import ru.tbank.petcare.domain.repository.ConnectivityRepository
import javax.inject.Inject

class IsOnlineUseCase @Inject constructor(
    private val connectivityRepository: ConnectivityRepository
) {
    operator fun invoke(): StateFlow<Boolean> = connectivityRepository.isOnline
}
