package com.ganecamp.domain.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStatusHelper @Inject constructor(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isConnectedNetwork = MutableStateFlow(false)
    val isConnectedNetwork: StateFlow<Boolean> = _isConnectedNetwork

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isConnectedNetwork.value = true
        }

        override fun onLost(network: Network) {
            _isConnectedNetwork.value = false
        }
    }

    init {
        val builder =
            NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        connectivityManager.registerNetworkCallback(builder.build(), networkCallback)

        val activeNetwork = connectivityManager.activeNetwork
        _isConnectedNetwork.value = activeNetwork != null
    }

}