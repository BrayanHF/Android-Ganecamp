package com.ganecamp.ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ganecamp.ui.theme.DarkGray
import com.ganecamp.ui.theme.White

// Todo: Independents buttons
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