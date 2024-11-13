package com.ganecamp.ui.lot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.data.firibase.model.Animal
import com.ganecamp.data.firibase.model.EventApplied
import com.ganecamp.data.firibase.model.Lot
import com.ganecamp.ui.general.IsLoading
import com.ganecamp.ui.general.ShowFirestoreError
import com.ganecamp.ui.navigation.LotFormNav
import com.ganecamp.ui.theme.LightBlue
import com.ganecamp.ui.theme.Red
import com.ganecamp.ui.theme.Typography
import com.ganecamp.utilities.enums.FirestoreRespond
import java.time.format.DateTimeFormatter
import java.util.Locale

//Todo: Change all the design and do the logic, this is for all the screen
@Composable
fun LotDetailScreen(navController: NavController, lotId: String?) {
    val viewModel: LotDetailViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading.collectAsState(initial = true)
    val lot by viewModel.lot.collectAsState()
    val events: List<EventApplied> by viewModel.events.collectAsState()
    val animals: List<Animal> by viewModel.animals.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(lotId) {
        if (lotId != null) {
            viewModel.loadLot(lotId)
            viewModel.loadEvents(lotId)
            viewModel.loadAnimals(lotId)
        } else {
            navController.popBackStack()
        }
    }

    var showError by remember { mutableStateOf(false) }
    LaunchedEffect(error) {
        if (error != FirestoreRespond.OK) {
            showError = true
        }
    }

    if (showError) {
        ShowFirestoreError(error = error, onDismiss = { showError = false })
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (isLoading) {
            IsLoading()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                LotInfo(lot!!)
                if (events.isNotEmpty()) {
                    LotEvents(events)
                }
                if (animals.isNotEmpty()) {
                    LotAnimals(animals)
                }
            }

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomEnd),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FloatingActionButton(
                    onClick = {
                        viewModel.deleteLot(lotId!!)
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(48.dp),
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    containerColor = Color.Transparent
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = stringResource(id = R.string.delete),
                        tint = Red,
                        modifier = Modifier.background(Color.Transparent)
                    )
                }

                FloatingActionButton(
                    onClick = { navController.navigate(LotFormNav(lotId)) },
                    modifier = Modifier.size(56.dp),
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    containerColor = Color.Transparent
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit2),
                        contentDescription = stringResource(id = R.string.edit),
                        tint = LightBlue,
                        modifier = Modifier.background(Color.Transparent)
                    )
                }
            }

        }
    }
}

@Composable
fun LotInfo(lotDetail: Lot) {
    Column(
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ID: ${lotDetail.id}",
            style = Typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.purchase_value),
                    style = Typography.bodyMedium
                )
                Text(
                    text = String.format(Locale.getDefault(), "%.2f", lotDetail.purchaseValue),
                    style = Typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.purchase_date),
                    style = Typography.bodyMedium
                )
                Text(
                    text = lotDetail.purchaseDate.toString().format(formatter),
                    style = Typography.bodyMedium
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.sale_value), style = Typography.bodyMedium
                )
                Text(
                    text = String.format(Locale.getDefault(), "%.2f", lotDetail.saleValue),
                    style = Typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.sale_date), style = Typography.bodyMedium
                )
                Text(
                    text = lotDetail.saleDate.toString().format(formatter),
                    style = Typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun LotEvents(events: List<EventApplied>) {
    Text(
        text = "Events",
        style = Typography.bodyMedium,
        modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp)
    )
    LazyColumn {
        items(events.size) {
            EventsLazyRow(event = events[it])
        }
    }
}

@Composable
fun LotAnimals(animals: List<Animal>) {
    Text(
        text = "Animals",
        style = Typography.bodyMedium,
        modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp)
    )
    animals.forEach {
        AnimalRow(it)
        if (it != animals.last()) {
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        }
    }
}

@Composable
fun AnimalRow(animal: Animal) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "ID: ${animal.id}", modifier = Modifier.weight(1f), style = Typography.bodyMedium
        )
        Text(
            text = animal.gender.name, modifier = Modifier.weight(1f), style = Typography.bodyMedium
        )
        Text(
            text = animal.state.name, modifier = Modifier.weight(1f), style = Typography.bodyMedium
        )
    }
}

@Composable
fun EventsLazyRow(event: EventApplied) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val formattedDate = event.date.toString().format(formatter)

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        modifier = Modifier
            .padding(8.dp)
            .width(300.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.title, style = Typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = formattedDate, style = Typography.titleSmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = event.description, style = Typography.titleSmall)
        }
    }
}