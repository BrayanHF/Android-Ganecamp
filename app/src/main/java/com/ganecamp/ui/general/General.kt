package com.ganecamp.ui.general

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ganecamp.R
import com.ganecamp.ui.theme.Black
import com.ganecamp.ui.theme.LightGreen
import com.ganecamp.ui.theme.Typography
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
    Column {
        Text(
            text = stringResource(id = textId), style = Typography.bodyLarge
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
            titleContentColor = Black,
            navigationIconContentColor = Black,
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
    selectedDate: ZonedDateTime, onDateChange: (ZonedDateTime) -> Unit, label: Int
) {
    val datePickerState = rememberDatePickerState()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val focusManager = LocalFocusManager.current

    var showDate by remember { mutableStateOf(false) }

    val formattedDate = selectedDate.format(formatter)

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
                IconButton(onClick = { showDate = !showDate }) {
                    Icon(
                        imageVector = Icons.Default.DateRange, contentDescription = "Select date"
                    )
                }
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
                        val dateZDT = date?.let {
                            Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC"))
                        }
                        if (dateZDT != null) {
                            onDateChange(dateZDT)
                        }
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