package com.ganecamp.ui.screen.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.ui.component.layout.SplitScreenLayout
import com.ganecamp.ui.component.misc.AppLogoWithSlogan
import com.ganecamp.ui.component.misc.IsLoading
import com.ganecamp.ui.navigation.screens.AnimalsNav
import com.ganecamp.ui.navigation.screens.LoginNav
import com.ganecamp.domain.enums.AuthRespond

@Composable
fun RegisterScreen(navController: NavController) {
    val viewModel: RegisterViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading.collectAsState()
    val authRespond by viewModel.authRespond.collectAsState()

    SplitScreenLayout(upperPart = { WelcomeRegister() }, lowerPart = { Register(viewModel) })

    LaunchedEffect(authRespond) {
        if (authRespond == AuthRespond.OK) {
            navController.navigate(AnimalsNav) {
                popUpTo(LoginNav) { inclusive = true }
            }
        }
    }

    if (isLoading) {
        IsLoading()
    } else {
        SplitScreenLayout(upperPart = { WelcomeRegister() }, lowerPart = { Register(viewModel) })
    }
}

@Composable
fun WelcomeRegister() {
    AppLogoWithSlogan()
}

//Todo: Contact us for the token
@Composable
fun Register(viewModel: RegisterViewModel) {
    val name by viewModel.name.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val token by viewModel.token.collectAsState()
    val showErrorDialog by viewModel.showErrorDialog.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    var repeatPassword by remember { mutableStateOf("") }

    Text(
        text = stringResource(id = R.string.welcome_to_ganecamp),
        style = MaterialTheme.typography.titleLarge
    )
    Text(
        text = stringResource(id = R.string.glad_register),
        style = MaterialTheme.typography.bodyMedium
    )
    Spacer(modifier = Modifier.padding(8.dp))
    OutlinedTextField(
        value = name,
        onValueChange = { viewModel.onNameChange(it) },
        label = { Text(stringResource(id = R.string.user_name)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = stringResource(id = R.string.user_name),
                modifier = Modifier.size(24.dp)
            )
        },
    )
    Spacer(modifier = Modifier.padding(8.dp))
    OutlinedTextField(value = phoneNumber,
        onValueChange = { viewModel.onPhoneNumberChange(it) },
        label = { Text(stringResource(id = R.string.phone_number)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_phone),
                contentDescription = stringResource(id = R.string.phone_number),
                modifier = Modifier.size(24.dp)
            )
        })
    Spacer(modifier = Modifier.padding(8.dp))
    OutlinedTextField(value = email,
        onValueChange = { viewModel.onEmailChange(it) },
        label = { Text(stringResource(id = R.string.email)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_email),
                contentDescription = stringResource(id = R.string.email),
                modifier = Modifier.size(24.dp)
            )
        })
    Spacer(modifier = Modifier.padding(8.dp))
    OutlinedTextField(value = password,
        onValueChange = { viewModel.onPasswordChange(it) },
        label = { Text(stringResource(id = R.string.password)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_my_password),
                contentDescription = stringResource(id = R.string.password),
                modifier = Modifier.size(24.dp)
            )
        },
        trailingIcon = {
            val icon: Int
            val iconText: Int
            if (passwordVisible) {
                icon = R.drawable.ic_see
                iconText = R.string.hide_password
            } else {
                icon = R.drawable.ic_hide
                iconText = R.string.show_password
            }
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = stringResource(id = iconText),
                    modifier = Modifier.size(24.dp)
                )
            }
        })
    Spacer(modifier = Modifier.padding(8.dp))
    OutlinedTextField(value = repeatPassword,
        onValueChange = { repeatPassword = it },
        label = { Text(stringResource(id = R.string.repeat_password)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_my_password),
                contentDescription = stringResource(id = R.string.password),
                modifier = Modifier.size(24.dp)
            )
        },
        isError = repeatPassword != password
    )
    Spacer(modifier = Modifier.padding(8.dp))
    OutlinedTextField(value = token,
        onValueChange = { viewModel.onTokenChange(it) },
        label = { Text(stringResource(id = R.string.token)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_token),
                contentDescription = stringResource(id = R.string.token),
                modifier = Modifier.size(24.dp)
            )
        })
    Text(
        text = stringResource(id = R.string.warning_token),
        style = MaterialTheme.typography.bodyMedium
    )
    Spacer(modifier = Modifier.padding(8.dp))
    Button(
        onClick = { viewModel.signUpWithEmailAndPassword() },
        enabled = name.isNotBlank() && phoneNumber.isNotBlank() && email.isNotBlank() && password.isNotBlank() && repeatPassword.isNotBlank() && token.isNotBlank() && repeatPassword == password
    ) {
        Text(
            text = stringResource(id = R.string.register), modifier = Modifier.padding(12.dp, 0.dp)
        )
    }

    if (showErrorDialog) {
        viewModel.signOut()
        AlertDialog(containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { viewModel.closeErrorDialog() },
            title = { /*Todo: Error message for register*/ },
            text = { /*Todo: Error message description for register*/ },
            confirmButton = {
                TextButton(onClick = { viewModel.closeErrorDialog() }) {
                    Text(stringResource(id = R.string.close))
                }
            })
    }
}