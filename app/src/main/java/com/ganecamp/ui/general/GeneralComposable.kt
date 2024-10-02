package com.ganecamp.ui.general

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ganecamp.R
import com.ganecamp.ui.theme.LightGreen
import com.ganecamp.ui.theme.Typography
import com.ganecamp.utilities.enums.FirestoreRespond
import com.google.firebase.Timestamp
import java.time.format.DateTimeFormatter
import java.util.Date

@Composable
fun IsLoading() {
    Box(
        Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun NoRegistered(textId: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = textId), style = Typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String, onBackClick: () -> Unit, content: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(text = title, style = Typography.titleSmall) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LightGreen,
            titleContentColor = MaterialTheme.colorScheme.secondary,
            navigationIconContentColor = MaterialTheme.colorScheme.secondary,
        ),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = stringResource(id = R.string.back),
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        actions = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    selectedDate: Timestamp, onDateChange: (Timestamp) -> Unit, label: Int
) {
    val datePickerState = rememberDatePickerState()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val focusManager = LocalFocusManager.current

    var showDate by remember { mutableStateOf(false) }

    val formattedDate = selectedDate.toDate().toString().format(formatter)

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(value = formattedDate,
            onValueChange = {},
            label = { Text(stringResource(id = label)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent { focusEvent ->
                    if (focusEvent.isFocused) {
                        showDate = true
                    }
                },
            trailingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = stringResource(id = R.string.calendar),
                    modifier = Modifier.size(24.dp)
                )
            })

        if (showDate) {
            DatePickerDialog(colors = DatePickerDefaults.colors()
                .copy(containerColor = MaterialTheme.colorScheme.background),
                onDismissRequest = {
                    showDate = false
                    focusManager.clearFocus()
                },
                confirmButton = {
                    Button(onClick = {
                        showDate = false
                        val date = datePickerState.selectedDateMillis
                        val timestamp = date?.let {
                            Timestamp(Date(it))
                        }
                        if (timestamp != null) onDateChange(timestamp)
                        focusManager.clearFocus()
                    }) {
                        Text(stringResource(id = R.string.confirm))
                    }
                }) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
fun NumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorMessage: String,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    var hasFocus by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(value = if (hasFocus) value else formatNumber(value),
            onValueChange = { newValue ->
                val sanitizedValue = sanitizeNumberDuringTyping(newValue)
                onValueChange(sanitizedValue)
            },
            label = { Text(text = label) },
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    hasFocus = it.isFocused
                    if (!it.isFocused) {
                        onValueChange(formatNumber(value))
                    }
                },
            trailingIcon = { trailingIcon?.invoke() })
        if (isError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun ShowFirestoreError(error: FirestoreRespond, onDismiss: () -> Unit) {
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