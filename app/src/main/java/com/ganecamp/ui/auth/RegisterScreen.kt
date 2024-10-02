package com.ganecamp.ui.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.ui.navigation.AnimalsNav
import com.ganecamp.ui.navigation.LoginNav
import com.ganecamp.utilities.enums.AuthRespond

@Composable
fun RegisterScreen(navController: NavController) {
    val viewModel: RegisterViewModel = hiltViewModel()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val name by viewModel.name.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val token by viewModel.token.collectAsState()
    val showErrorDialog by viewModel.showErrorDialog.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val authRespond by viewModel.authRespond.collectAsState()

    LaunchedEffect(authRespond) {
        if (authRespond == AuthRespond.OK) {
            navController.navigate(AnimalsNav) {
                popUpTo(LoginNav) { inclusive = true }
            }
        }
    }


    var passwordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {

        OutlinedTextField(
            value = token,
            onValueChange = { viewModel.onTokenChange(it) },
            label = { Text("Token") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { viewModel.onPhoneNumberChange(it) },
            label = { Text("Phone Number") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(value = password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val icon = if (passwordVisible) R.drawable.ic_cow else R.drawable.ic_bull
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = "Toggle password visibility"
                    )
                }
            })

        OutlinedButton(
            onClick = { viewModel.signUpWithEmailAndPassword() }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
        if (showErrorDialog) {
            viewModel.signOut()
            AlertDialog(containerColor = MaterialTheme.colorScheme.background,
                onDismissRequest = { viewModel.closeErrorDialog() },
                title = { Text("Error") },
                text = { Text(authRespond.toString()) },
                confirmButton = {
                    TextButton(onClick = { viewModel.closeErrorDialog() }) {
                        Text("Cerrar")
                    }
                })
        }

        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
    }
}