package com.ganecamp.ui.screen.lot

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.domain.model.Lot
import com.ganecamp.ui.component.dialog.ErrorDialog
import com.ganecamp.ui.component.misc.IsLoading
import com.ganecamp.ui.component.misc.NoRegistered
import com.ganecamp.ui.navigation.screens.LotDetailNav
import com.ganecamp.ui.navigation.screens.LotFormNav
import com.ganecamp.ui.theme.LightGray
import com.ganecamp.ui.theme.Typography

@Composable
fun LotScreen(navController: NavController) {
    val viewModel: LotViewModel = hiltViewModel()
    val lots by viewModel.lots.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState(initial = true)
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLots()
    }

    if (error != null) {
        ErrorDialog(error = error!!, onDismiss = { viewModel.dismissError() })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 0.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (isLoading) {
            IsLoading()
        } else {
            LotList(navController, lots)
        }

        FloatingActionButton(
            onClick = { navController.navigate(LotFormNav(null)) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 12.dp, end = 8.dp),
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.add),
            )
        }
    }
}

@Composable
fun LotList(navController: NavController, lots: List<Lot>) {
    if (lots.isEmpty()) {
        NoRegistered(textId = R.string.no_lots)
        return
    }

    val columns: Int =
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(lots) { lot ->
            LotCard(navController = navController, lot = lot)
        }
    }
}


//Todo: Think in a better design for this card
@Composable
fun LotCard(navController: NavController, lot: Lot) {
    Card(
        onClick = { navController.navigate(LotDetailNav(lot.id!!)) },
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = lot.name,
                style = Typography.titleMedium,
            )
            Text(
                text = stringResource(id = R.string.animal_count) + ": ${lot.animalCount}",
                style = Typography.bodyMedium,
            )
            val (colorState, textState) = if (lot.sold) {
                Pair(LightGray, R.string.sold)
            } else {
                Pair(Color.Green, R.string.not_sold)
            }
            Box(
                modifier = Modifier
                    .background(
                        color = colorState.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(id = textState),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}