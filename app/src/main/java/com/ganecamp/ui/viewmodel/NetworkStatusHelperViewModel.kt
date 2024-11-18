package com.ganecamp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.ganecamp.domain.network.NetworkStatusHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NetworkStatusHelperViewModel @Inject constructor(
    networkStatusHelper: NetworkStatusHelper
) : ViewModel() {

    val isConnectedNetwork: StateFlow<Boolean> = networkStatusHelper.isConnectedNetwork

}