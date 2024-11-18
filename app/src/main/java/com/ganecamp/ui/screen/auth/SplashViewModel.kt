package com.ganecamp.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.data.firibase.FarmSessionManager
import com.ganecamp.domain.services.FarmService
import com.ganecamp.domain.services.GanecampUserService
import com.ganecamp.data.firibase.model.Farm
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val ganecampUserService: GanecampUserService,
    private val farmService: FarmService,
    private val farmSessionManager: FarmSessionManager
) : ViewModel() {

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val _farm = MutableStateFlow<Farm?>(null)
    val farm: StateFlow<Farm?> = _farm

    private val _loadingComplete = MutableStateFlow(false)
    val loadingComplete: StateFlow<Boolean> = _loadingComplete

    fun loadFarmSessionManger() {
        viewModelScope.launch {
            val user = firebaseAuth.currentUser
            val userEmail = user?.email
            userEmail?.let { email ->
                val userRespond = ganecampUserService.getUserByEmail(email)
                userRespond.first?.farmToken?.let { token ->
                    val farmRespond = farmService.getFarmByToken(token)
                    farmRespond.first?.let { farm ->
                        farmSessionManager.setFarm(farm)
                        _farm.value = farm
                        _currentUser.value = user
                    }
                }
            }
            _loadingComplete.value = true
        }
    }

}