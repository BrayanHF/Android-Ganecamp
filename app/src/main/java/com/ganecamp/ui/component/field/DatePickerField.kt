package com.ganecamp.ui.component.field

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ganecamp.R
import com.ganecamp.ui.theme.White
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    selectedDate: Instant, onDateChange: (Instant) -> Unit, label: Int, icon: Int? = null
) {
    val datePickerState = rememberDatePickerState()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.of("UTC"))
    val focusManager = LocalFocusManager.current

    var showDate by remember { mutableStateOf(false) }

    val formattedDate = formatter.format(selectedDate)

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
                    painter = if (icon == null) painterResource(id = R.drawable.ic_calendar)
                    else painterResource(id = icon),
                    contentDescription = stringResource(id = R.string.calendar),
                    modifier = Modifier.size(24.dp)
                )
            })

        if (showDate) {
            DatePickerDialog(colors = DatePickerDefaults.colors().copy(
                containerColor = White
            ), onDismissRequest = {
                showDate = false
                focusManager.clearFocus()
            }, confirmButton = {
                Button(onClick = {
                    showDate = false
                    val date = datePickerState.selectedDateMillis
                    val instant = date?.let { Instant.ofEpochMilli(it) }
                    if (instant != null) onDateChange(instant)
                    focusManager.clearFocus()
                }) {
                    Text(stringResource(id = R.string.confirm))
                }
            }) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors().copy(
                        containerColor = White
                    ),

                    )
            }
        }
    }
}