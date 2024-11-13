package com.ganecamp.ui.lot

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.data.firibase.model.Lot
import com.ganecamp.ui.general.IsLoading
import com.ganecamp.ui.general.NoRegistered
import com.ganecamp.ui.general.ShowFirestoreError
import com.ganecamp.ui.navigation.LotDetailNav
import com.ganecamp.ui.navigation.LotFormNav
import com.ganecamp.ui.theme.LightGray
import com.ganecamp.ui.theme.Typography
import com.ganecamp.utilities.enums.FirestoreRespond

//Todo: Maybe change whole design and change the add bottom
@Composable
fun LotScreen(navController: NavController) {
    val viewModel: LotViewModel = hiltViewModel()
    val lots by viewModel.lots.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState(initial = true)
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLots()
    }

    var showError by remember { mutableStateOf(false) }
    LaunchedEffect(error) {
        if (error != FirestoreRespond.OK) {
            showError = true
        }
    }

    if (showError) {
        ShowFirestoreError(error = error, onDismiss = { showError = false })
        showError = false
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
                .padding(16.dp)
                .align(Alignment.BottomEnd)
                .size(64.dp),
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            containerColor = Color.Transparent
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = stringResource(id = R.string.add),
                modifier = Modifier.background(Color.Transparent)
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

@Composable
fun LotCard(navController: NavController, lot: Lot) {
    Card(
        onClick = { navController.navigate(LotDetailNav(lot.id!!)) },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.padding(8.dp)) {
                Text(
                    text = "ID: ${lot.id}",
                    style = Typography.titleMedium,
                )
                Text(
                    text = stringResource(id = R.string.animals) + ": ${lot.numberAnimals}",
                    style = Typography.bodyMedium,
                )
            } // Todo: This box needs dynamic color and text based on the status of the sold lot
            Box(
                modifier = Modifier
                    .background(
                        color = LightGray.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.sold),
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}