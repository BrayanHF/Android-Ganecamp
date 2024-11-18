package com.ganecamp.ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ActionButton(
    text: String, color: Color, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = { onClick() },
        shape = RoundedCornerShape(50),
        border = BorderStroke(width = 1.dp, color = color),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = color
        ),
        modifier = modifier
    ) {
        Text(text = text)
    }
}