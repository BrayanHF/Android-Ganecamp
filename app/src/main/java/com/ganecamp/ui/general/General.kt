package com.ganecamp.ui.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.domain.model.Description
import com.ganecamp.ui.theme.Black
import com.ganecamp.ui.theme.LightGreen
import com.ganecamp.ui.theme.Typography
import com.ganecamp.ui.theme.White
import java.time.format.DateTimeFormatter

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
        Modifier.fillMaxSize(), contentAlignment = Alignment.Center
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

@Composable
fun GeneralDescriptionCard(description: Description) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val formattedDate = description.date.format(formatter)

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        modifier = Modifier
            .padding(8.dp)
            .width(300.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = description.title, style = Typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = formattedDate, style = Typography.titleSmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description.description, style = Typography.titleSmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController, content: @Composable RowScope.() -> Unit
) {
    TopAppBar(title = {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            content = content
        )
    }, colors = TopAppBarColors(LightGreen, LightGreen, Black, Black, LightGreen),
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = stringResource(id = R.string.back),
                    modifier = Modifier.size(24.dp)
                )
            }
        })
}

@Composable
fun DefaultTopBarContent() {
    Text(text = "tittle")
}
