package com.ganecamp.ui.component.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ganecamp.ui.theme.Green
import com.ganecamp.ui.theme.LightGray

@Composable
fun TwoColumns(
    titleLeft: String,
    valueLeft: String,
    titleRight: String,
    valueRight: String,
    isClickableLeft: Boolean = false,
    onClickLeft: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .clickable(enabled = isClickableLeft, onClick = { onClickLeft?.invoke() })
                .background(
                    color = if (isClickableLeft) Green.copy(alpha = 0.2f) else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 8.dp,
                    end = if (isClickableLeft) 16.dp else 8.dp
                )
        ) {
            Text(
                text = titleLeft, style = MaterialTheme.typography.bodySmall, color = LightGray
            )
            Text(
                text = valueLeft, style = MaterialTheme.typography.bodyMedium
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(
                text = titleRight, style = MaterialTheme.typography.bodySmall, color = LightGray
            )
            Text(
                text = valueRight, style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}