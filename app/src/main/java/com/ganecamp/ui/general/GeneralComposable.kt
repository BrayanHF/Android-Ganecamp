package com.ganecamp.ui.general

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ganecamp.R
import com.ganecamp.ui.theme.Black
import com.ganecamp.ui.theme.DarkGray
import com.ganecamp.ui.theme.Green
import com.ganecamp.ui.theme.LightGreen
import com.ganecamp.ui.theme.Typography
import com.ganecamp.ui.theme.White
import com.ganecamp.utilities.enums.FirestoreRespond
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.time.Instant
import java.time.ZoneId
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
fun GeneralTopBar(
    title: String, onBackClick: () -> Unit, content: @Composable RowScope.() -> Unit = {}
) {
    val networkStatusHelper: NetworkStatusHelperViewModel = hiltViewModel()
    val isConnectedNetwork by networkStatusHelper.isConnectedNetwork.collectAsState()

    val colorTop = remember(isConnectedNetwork) {
        if (isConnectedNetwork) LightGreen else White
    }

    BarColor(colorTop)

    TopAppBar(
        title = { Text(text = title, style = Typography.titleSmall) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorTop,
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

@Composable
fun TwoPartScreen(upperPart: @Composable () -> Unit, lowerPart: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1 / 3f)
                .background(White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Green, shape = RoundedCornerShape(
                            topStart = CornerSize(0.dp),
                            topEnd = CornerSize(0.dp),
                            bottomStart = CornerSize(0.dp),
                            bottomEnd = CornerSize(64.dp)
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    upperPart()
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Green)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        White, shape = RoundedCornerShape(
                            topStart = CornerSize(64.dp),
                            topEnd = CornerSize(0.dp),
                            bottomStart = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp)
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    lowerPart()
                }
            }
        }
    }
}

@Composable
fun LogoAndSlogan(color: Color = Black) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.ic_cow_login),
            modifier = Modifier.height(64.dp),
            tint = color
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Column {
            Text(
                text = stringResource(id = R.string.ganecamp),
                style = MaterialTheme.typography.titleLarge,
                color = color
            )
            Text(
                text = stringResource(id = R.string.slogan),
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
        }
    }
}

@Composable
fun BarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = color, darkIcons = true
    )
}

@Composable
fun ToggleButtons(
    txtFirstButton: String, txtSecondButton: String, onSelectionChange: (Boolean) -> Unit
) {
    val isFirstSelected = remember { mutableStateOf(true) }
    val options = listOf(txtFirstButton, txtSecondButton)

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
    ) {
        options.forEach { option ->
            val isSelected =
                (option == txtFirstButton && isFirstSelected.value) || (option == txtSecondButton && !isFirstSelected.value)

            OutlinedButton(
                onClick = {
                    isFirstSelected.value = option == txtFirstButton
                    onSelectionChange(isFirstSelected.value)
                }, colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (isSelected) White else DarkGray,
                    containerColor = if (isSelected) DarkGray else Color.Transparent
                ), border = BorderStroke(
                    width = 1.0.dp, color = DarkGray
                ), modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = option, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                )
            }
        }
    }
}