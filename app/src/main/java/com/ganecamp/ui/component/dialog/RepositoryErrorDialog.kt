package com.ganecamp.ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.ganecamp.domain.enums.RepositoryRespond

@Composable
fun RepositoryErrorDialog(error: RepositoryRespond, onDismiss: () -> Unit) {
    AlertDialog(containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = { onDismiss() },
        title = { Text("Error") },
        text = { Text(error.toString()) },
        confirmButton = {
            TextButton(onClick = { }) {
                Text("Cerrar")
            }
        })
}