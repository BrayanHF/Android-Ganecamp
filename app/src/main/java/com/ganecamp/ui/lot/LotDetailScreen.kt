package com.ganecamp.ui.lot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.model.Description
import com.ganecamp.domain.model.LotDetail
import com.ganecamp.ui.general.GeneralDescriptionCard
import com.ganecamp.ui.general.IsLoading
import com.ganecamp.ui.navigation.ScreenInternal
import com.ganecamp.ui.theme.LightBlue
import com.ganecamp.ui.theme.Red
import com.ganecamp.ui.theme.Typography
import com.ganecamp.ui.theme.White
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun LotDetailScreen(navController: NavController, lotId: Int) {
    val viewModel: LotDetailViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading.observeAsState(initial = true)
    val lotDetail by viewModel.lot.observeAsState(
        initial = LotDetail(
            lotId, 0.0, ZonedDateTime.now(), 0.0, ZonedDateTime.now()
        )
    )
    val events: List<Description> by viewModel.events.observeAsState(initial = emptyList())
    val animals: List<Animal> by viewModel.animals.observeAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(White)
    ) {
        if (isLoading) {
            IsLoading()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                LotInfo(lotDetail)
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
                        viewModel.deleteLot(lotId)
                        navController.navigate("lot") {
                            popUpTo(ScreenInternal.LotDetail.route) { inclusive = true }
                        }
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
                    onClick = { navController.navigate("formLot/${lotId}") },
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
fun LotDetailTopBarContent() {
    Text(text = stringResource(id = R.string.lot_detail))
}

@Composable
fun LotInfo(lotDetail: LotDetail) {
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
                    text = lotDetail.purchaseDate.format(formatter), style = Typography.bodyMedium
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
                    text = lotDetail.saleDate.format(formatter), style = Typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun LotEvents(events: List<Description>) {
    Text(
        text = "Events",
        style = Typography.bodyMedium,
        modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp)
    )
    LazyColumn {
        items(events.size) {
            GeneralDescriptionCard(description = events[it])
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