package com.ganecamp.ui.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ganecamp.ui.theme.LightGreenAlpha
import com.ganecamp.ui.theme.Typography
import com.ganecamp.ui.theme.White

@Composable
fun GeneralBox(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(White)
    ) {
        content()
    }
}

@Composable
fun IsLoading() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
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

@Composable
fun GeneralSurface(onClick: () -> Unit, content: @Composable () -> Unit) {
    Surface(
        onClick = { onClick() },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxSize(),
        shadowElevation = 4.dp,
        color = White
    ) {
        content()
    }
}
