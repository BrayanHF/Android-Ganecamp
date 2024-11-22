package com.ganecamp.ui.screen.lot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.model.EventApplied
import com.ganecamp.domain.model.Lot
import com.ganecamp.ui.component.bar.GenericTopBar
import com.ganecamp.ui.component.button.ActionButton
import com.ganecamp.ui.component.card.AnimalCard
import com.ganecamp.ui.component.card.InfoCard
import com.ganecamp.ui.component.dialog.ErrorDialog
import com.ganecamp.ui.component.layout.SectionWithLazyColumn
import com.ganecamp.ui.component.layout.SectionWithLazyRow
import com.ganecamp.ui.component.layout.TwoColumns
import com.ganecamp.ui.component.misc.IsLoading
import com.ganecamp.ui.navigation.screens.AnimalDetailNav
import com.ganecamp.ui.navigation.screens.EventAddFormNav
import com.ganecamp.ui.navigation.screens.LotFormNav
import com.ganecamp.ui.theme.LightBlue
import com.ganecamp.ui.theme.LightGray
import com.ganecamp.ui.theme.Red
import com.ganecamp.ui.util.TimeUtil
import com.ganecamp.ui.util.formatNumber

@Composable
fun LotDetailScreen(navController: NavController, lotId: String) {
    val viewModel: LotDetailViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading.collectAsState(initial = true)
    val lot by viewModel.lot.collectAsState()
    val events by viewModel.events.collectAsState()
    val animals by viewModel.animals.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(lotId) {
        viewModel.loadLot(lotId)
        viewModel.loadEvents(lotId)
        viewModel.loadAnimals(lotId)
    }

    if (error != null) {
        ErrorDialog(error = error!!, onDismiss = { viewModel.dismissError() })
    }

    Scaffold(topBar = {
        GenericTopBar(
            onBackClick = { navController.popBackStack() },
            title = stringResource(id = R.string.lot_detail),
        )
    }) { innerPadding ->
        if (isLoading) {
            IsLoading()
        } else {
            lot?.let { lot ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    LotDetailContent(
                        lot = lot,
                        events = events,
                        animals = animals,
                        navController = navController,
                    )
                    ActionButton(
                        text = stringResource(R.string.delete_lot),
                        onClick = {
                            viewModel.deleteLot()
                            navController.popBackStack()
                        },
                        color = Red,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(16.dp, 8.dp)
                    )
                }
            } ?: navController.popBackStack()
        }
    }
}

@Composable
fun LotDetailContent(
    lot: Lot,
    events: List<EventApplied>,
    animals: List<Animal>,
    navController: NavController,
) {
    LotInfo(lotDetail = lot, navController = navController)

    SectionWithLazyRow(titleSection = stringResource(R.string.events),
        itemsSection = events,
        cardItem = { event ->
            InfoCard(
                onClick = { //Todo: Add navigation to event detail
                }, info = event.title, date = event.date
            )
        },
        textButtonAdd = stringResource(R.string.add_event),
        onClickAdd = {
            lot.id?.let { lotId ->
                navController.navigate(EventAddFormNav(lotId, EntityType.Lot))
            }
        })

    SectionWithLazyColumn(titleSection = stringResource(R.string.animals),
        itemsSection = animals,
        cardItem = { animal ->
            AnimalCard(animal = animal, onClick = {
                animal.id?.let { animalId ->
                    navController.navigate(AnimalDetailNav(animalId))
                }
            })
        })

}

@Composable
fun LotInfo(lotDetail: Lot, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = lotDetail.name, style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.state),
                        style = MaterialTheme.typography.bodySmall,
                        color = LightGray
                    )
                    Text(
                        text = if (lotDetail.sold) stringResource(R.string.sold) else stringResource(
                            R.string.not_sold
                        ), style = MaterialTheme.typography.bodyMedium
                    )
                }
                ActionButton(
                    text = stringResource(id = R.string.edit_lot), color = LightBlue, onClick = {
                        navController.navigate(LotFormNav(lotDetail.id))
                    }, modifier = Modifier.weight(1f)
                )
            }

            TwoColumns(
                titleLeft = stringResource(R.string.purchase_date),
                valueLeft = TimeUtil.formatter.format(lotDetail.purchaseDate),
                titleRight = stringResource(R.string.purchase_value),
                valueRight = formatNumber(lotDetail.purchaseValue.toString())
            )

            if (lotDetail.sold) {
                TwoColumns(
                    titleLeft = stringResource(R.string.sale_date),
                    valueLeft = TimeUtil.formatter.format(lotDetail.saleDate),
                    titleRight = stringResource(R.string.sale_value),
                    valueRight = formatNumber(lotDetail.saleValue.toString())
                )
            }

            TwoColumns(
                titleLeft = stringResource(R.string.purchased_animals),
                valueLeft = lotDetail.purchasedAnimals.toString(),
                titleRight = stringResource(R.string.animal_count),
                valueRight = lotDetail.animalCount.toString()
            )

        }
    }
}