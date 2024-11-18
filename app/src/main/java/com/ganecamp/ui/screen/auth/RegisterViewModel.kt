package com.ganecamp.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.data.firibase.FirebaseAuthentication
import com.ganecamp.domain.enums.AuthRespond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuthentication
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _token = MutableStateFlow("")
    val token: StateFlow<String> = _token

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

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun onTokenChange(token: String) {
        _token.value = token
    }

    fun signUpWithEmailAndPassword() {
        viewModelScope.launch {
            _isLoading.value = true
            val respond = auth.signUpWithEmailAndPassword(
                _email.value, _password.value, _name.value, _phoneNumber.value, _token.value
            )
            _authRespond.value = respond
            _isLoading.value = false
            if (respond != AuthRespond.OK) {
                showErrorDialog()
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }

}