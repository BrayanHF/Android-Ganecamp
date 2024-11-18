package com.ganecamp.ui.component.misc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ganecamp.R
import com.ganecamp.ui.theme.Black

@Composable
fun AppLogoWithSlogan(color: Color = Black) {
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