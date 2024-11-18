package com.ganecamp.ui.component.bar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ganecamp.R
import com.ganecamp.ui.theme.LightGreen
import com.ganecamp.ui.theme.Typography
import com.ganecamp.ui.theme.White
import com.ganecamp.ui.viewmodel.NetworkStatusHelperViewModel

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