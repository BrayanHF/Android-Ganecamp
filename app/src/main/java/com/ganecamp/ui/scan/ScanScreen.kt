package com.ganecamp.ui.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.ui.general.ShowFirestoreError
import com.ganecamp.ui.navigation.AnimalDetailNav
import com.ganecamp.ui.navigation.AnimalFormNav
import com.ganecamp.utilities.enums.FirestoreRespond

@Composable
fun ScanScreen(navController: NavController) {
    val viewModel: ScanViewModel = hiltViewModel()

    val rfidText = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val showErrorDialog by viewModel.showErrorDialog.collectAsState(false)
    val showConfirmDialog by viewModel.showConfirmDialog.collectAsState(false)
    val animalId by viewModel.animalId.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.hide()
    }

    LaunchedEffect(animalId) {
        if (animalId != null) {
            navController.navigate(AnimalDetailNav(animalId!!))
            viewModel.resetAnimalId()
        }
    }

    var showError by remember { mutableStateOf(false) }
    LaunchedEffect(error) {
        if (error != FirestoreRespond.OK) {
            showError = true
        }
    }

    if (showError) {
        ShowFirestoreError(error = error, onDismiss = { showError = false })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Por favor, escanee el TAG o escriba el ID del TAG:")

        OutlinedTextField(
            value = rfidText.value,
            onValueChange = { newValue ->
                rfidText.value = newValue
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Enter) {
                        viewModel.onTagReceived(rfidText.value)
                        true
                    } else {
                        false
                    }
                },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = {
                viewModel.onTagReceived(rfidText.value)
            })
        )
    }

    if (showErrorDialog) {
        AlertDialog(containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { viewModel.closeErrorDialog() },
            title = { Text("Error") },
            text = { Text("Hubo un error al buscar el TAG.\n$error") },
            confirmButton = {
                TextButton(onClick = { viewModel.closeErrorDialog() }) {
                    Text("Cerrar")
                }
            })
    }

    if (showConfirmDialog) {
        AlertDialog(containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { viewModel.closeConfirmDialog() },
            title = { Text("TAG No Encontrado") },
            text = { Text("Este TAG no está en la base de datos. ¿Deseas agregarlo?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.closeConfirmDialog()
                    navController.navigate(AnimalFormNav(animalId, rfidText.value))
                }) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.closeConfirmDialog() }) {
                    Text("Cancelar")
                }
            })
    }
}