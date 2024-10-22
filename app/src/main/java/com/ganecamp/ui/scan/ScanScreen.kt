package com.ganecamp.ui.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.ui.general.BarColor
import com.ganecamp.ui.general.ShowFirestoreError
import com.ganecamp.ui.navigation.AnimalDetailNav
import com.ganecamp.ui.navigation.AnimalFormNav
import com.ganecamp.ui.theme.White

@Composable
fun ScanScreen(navController: NavController) {
    val viewModel: ScanViewModel = hiltViewModel()
    val showErrorDialog by viewModel.showErrorDialog.collectAsState(false)
    val showConfirmDialog by viewModel.showConfirmDialog.collectAsState(false)
    val animalId by viewModel.animalId.collectAsState()
    val error by viewModel.error.collectAsState()

    val rfidText = remember { mutableStateOf("") }

    BarColor(White)

    LaunchedEffect(animalId) {
        if (animalId != null) {
            navController.navigate(AnimalDetailNav(animalId!!))
            viewModel.resetAnimalId()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.scan_rfid),
            style = MaterialTheme.typography.bodySmall
        )
        OutlinedTextField(
            value = rfidText.value,
            onValueChange = { tagValue ->
                rfidText.value = tagValue
            },
            label = { Text(stringResource(id = R.string.tag_rfid)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                viewModel.onTagReceived(rfidText.value)
            }),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tag),
                    contentDescription = stringResource(id = R.string.tag_rfid),
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }

    if (showErrorDialog) {
        ShowFirestoreError(error) { viewModel.closeErrorDialog() }
    }

    if (showConfirmDialog) {
        AlertDialog(containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { viewModel.closeConfirmDialog() },
            title = { Text(stringResource(id = R.string.tag_not_found)) },
            text = { Text(stringResource(id = R.string.add_tag_confirm)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.closeConfirmDialog()
                    navController.navigate(AnimalFormNav(animalId, rfidText.value))
                }) {
                    Text(stringResource(id = R.string.add))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.closeConfirmDialog() }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }
}