package com.ganecamp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.data.firibase.FirebaseAuthentication
import com.ganecamp.utilities.enums.AuthRespond
import com.ganecamp.utilities.enums.AuthRespond.OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuthentication
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _authRespond = MutableStateFlow(AuthRespond.UNKNOWN_ERROR)
    val authRespond: StateFlow<AuthRespond> = _authRespond

    private fun showErrorDialog() {
        _showErrorDialog.value = true
    }

    fun closeErrorDialog() {
        _showErrorDialog.value = false
    }

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun signInWithEmailAndPassword() {
        viewModelScope.launch {
            _isLoading.value = true
            val respond = auth.signInWithEmailAndPassword(_email.value, _password.value)
            _authRespond.value = respond
            _isLoading.value = false
            if (respond != OK) {
                showErrorDialog()
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }

}