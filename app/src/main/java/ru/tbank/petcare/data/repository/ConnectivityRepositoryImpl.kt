package ru.tbank.petcare.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.provider.SyncStateContract.Helpers.update
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.tbank.petcare.domain.repository.ConnectivityRepository
import javax.inject.Inject

class ConnectivityRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : ConnectivityRepository {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _isOnline = MutableStateFlow(false)
    override val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            update()
        }
        override fun onLost(network: Network) {
            update()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            update()
        }
    }
    init {
        update()
        connectivityManager.registerDefaultNetworkCallback(callback)
    }
    private fun update() {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        _isOnline.value =
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
