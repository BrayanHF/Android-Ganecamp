package com.ganecamp.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.result.OperationResult.Error
import com.ganecamp.domain.result.OperationResult.Success
import com.ganecamp.domain.usecase.farmsesion.LoadFarmSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loadFarmSessionUseCase: LoadFarmSessionUseCase
) : ViewModel() {

    private val _load = MutableStateFlow(false)
    val load: StateFlow<Boolean> = _load

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadFarmSessionManger() {
        viewModelScope.launch {
            _isLoading.value = true
            when (loadFarmSessionUseCase.invoke()) {
                is Success -> _load.value = true
                is Error -> _load.value = false
            }
            _isLoading.value = false
        }
    }

}