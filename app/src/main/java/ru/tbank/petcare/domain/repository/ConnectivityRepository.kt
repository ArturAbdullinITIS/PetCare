package ru.tbank.petcare.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface ConnectivityRepository {
    val isOnline: StateFlow<Boolean>
}
