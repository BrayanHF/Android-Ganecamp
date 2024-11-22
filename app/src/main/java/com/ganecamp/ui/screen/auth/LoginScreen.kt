package com.ganecamp.ui.screen.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
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
import com.ganecamp.ui.component.dialog.ErrorDialog
import com.ganecamp.ui.component.layout.SplitScreenLayout
import com.ganecamp.ui.component.misc.AppLogoWithSlogan
import com.ganecamp.ui.component.misc.IsLoading
import com.ganecamp.ui.navigation.screens.AnimalsNav
import com.ganecamp.ui.navigation.screens.LoginNav
import com.ganecamp.ui.navigation.screens.RegisterNav

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: LoginViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading.collectAsState()
    val success by viewModel.success.collectAsState()

    LaunchedEffect(success) {
        if (success) {
            navController.navigate(AnimalsNav) {
                popUpTo(LoginNav) { inclusive = true }
            }
        }
    }

    if (isLoading) {
        IsLoading()
    } else {
        SplitScreenLayout(upperPart = { WelcomeLogin() },
            lowerPart = { Login(navController, viewModel) })
    }
}

@Composable
fun WelcomeLogin() {
    AppLogoWithSlogan()
}

@Composable
fun Login(navController: NavController, viewModel: LoginViewModel) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val error by viewModel.error.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }

    Text(
        text = stringResource(id = R.string.welcome), style = MaterialTheme.typography.titleLarge
    )
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
    Button(
        onClick = { viewModel.signInWithEmailAndPassword() },
        enabled = email.isNotBlank() && password.isNotBlank()
    ) {
        Text(
            text = stringResource(id = R.string.login), modifier = Modifier.padding(12.dp, 0.dp)
        )
    }
    Spacer(modifier = Modifier.padding(8.dp))
    TextButton(onClick = { /*Todo: Forgot password, the logic and the page*/ }) {
        Text(text = stringResource(id = R.string.forgot_password))
    }
    Spacer(modifier = Modifier.padding(8.dp))
    TextButton(onClick = { navController.navigate(RegisterNav) }) {
        Text(text = stringResource(id = R.string.no_account))
    }

    if (error != null) {
        viewModel.signOut()
        ErrorDialog(error = error!!, onDismiss = { viewModel.dismissError() })
    }

}