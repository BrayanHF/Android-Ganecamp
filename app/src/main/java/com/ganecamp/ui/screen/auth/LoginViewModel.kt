package com.ganecamp.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.enums.ErrorType
import com.ganecamp.domain.result.OperationResult.Error
import com.ganecamp.domain.result.OperationResult.Success
import com.ganecamp.domain.usecase.auth.SignInUseCase
import com.ganecamp.domain.usecase.auth.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase, private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<ErrorType?>(null)
    val error: StateFlow<ErrorType?> = _error

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun dismissError() {
        _error.value = null
    }

    fun signInWithEmailAndPassword() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = signInUseCase(_email.value, _password.value)) {
                is Success -> _success.value = true
                is Error -> _error.value = result.errorType
            }
            _isLoading.value = false
        }
    }

    fun signOut() {
        signOutUseCase()
    }

}