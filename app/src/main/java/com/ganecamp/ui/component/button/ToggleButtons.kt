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

@Composable
fun ToggleButtons(
    txtLeftButton: String,
    txtRightButton: String,
    onClickLeft: (Boolean) -> Unit,
    onCLickRight: (Boolean) -> Unit,
    leftButtonSelectedFirsts: Boolean = true
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
    ) {
        val isLeftSelected = remember { mutableStateOf(leftButtonSelectedFirsts) }
        val isRightSelected = remember { mutableStateOf(!leftButtonSelectedFirsts) }
        OutlinedButton(
            onClick = {
                isLeftSelected.value = true
                isRightSelected.value = false
                onClickLeft(isLeftSelected.value)
            }, colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (isLeftSelected.value) White else DarkGray,
                containerColor = if (isLeftSelected.value) DarkGray else Color.Transparent
            ), border = BorderStroke(
                width = 1.0.dp, color = DarkGray
            ), modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(
                text = txtLeftButton,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        OutlinedButton(
            onClick = {
                isLeftSelected.value = false
                isRightSelected.value = true
                onCLickRight(isRightSelected.value)
            }, colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (isRightSelected.value) White else DarkGray,
                containerColor = if (isRightSelected.value) DarkGray else Color.Transparent
            ), border = BorderStroke(
                width = 1.0.dp, color = DarkGray
            ), modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(
                text = txtRightButton,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}