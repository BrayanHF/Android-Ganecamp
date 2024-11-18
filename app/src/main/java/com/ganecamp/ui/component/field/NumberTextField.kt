package com.ganecamp.ui.component.field

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import com.ganecamp.ui.util.formatNumber
import com.ganecamp.ui.util.sanitizeNumberDuringTyping

@Composable
fun NumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
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
            isError = false,
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
    }
}