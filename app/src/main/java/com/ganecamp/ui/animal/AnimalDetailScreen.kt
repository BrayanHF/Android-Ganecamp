package com.ganecamp.ui.animal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ganecamp.R
import com.ganecamp.domain.model.AnimalDetail
import com.ganecamp.domain.model.Description
import com.ganecamp.domain.model.Weight
import com.ganecamp.ui.general.IsLoading
import com.ganecamp.ui.general.TopBar
import com.ganecamp.ui.general.formatNumber
import com.ganecamp.ui.navigation.AnimalDetailNav
import com.ganecamp.ui.navigation.AnimalFormNav
import com.ganecamp.ui.navigation.AnimalsNav
import com.ganecamp.ui.navigation.LotDetailNav
import com.ganecamp.ui.theme.Green
import com.ganecamp.ui.theme.LightBlue
import com.ganecamp.ui.theme.LightGray
import com.ganecamp.ui.theme.Red
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AnimalDetailScreen(navController: NavHostController, animalId: Int) {
    val viewModel: AnimalDetailViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading.observeAsState(initial = true)
    val animalDetail: AnimalDetail by viewModel.animal.observeAsState(
        initial = AnimalDetail(
            "", animalId, Gender.Male, ZonedDateTime.now(), 0.0, 0.0, State.Healthy
        )
    )
    val lotId by viewModel.lotId.observeAsState(initial = 0)
    val vaccines: List<Description> by viewModel.vaccines.observeAsState(initial = emptyList())
    val events: List<Description> by viewModel.events.observeAsState(initial = emptyList())
    val weights: List<Weight> by viewModel.weights.observeAsState(initial = emptyList())
    val age by viewModel.ageAnimal.observeAsState(initial = Triple(0, 0, 0))
    val weightValue by viewModel.weightValue.observeAsState(initial = 4000f)



    LaunchedEffect(animalId) {
        viewModel.loadAnimal()
        viewModel.loadLotId()
        viewModel.loadVaccines()
        viewModel.loadEvents()
        viewModel.loadWeights()
        viewModel.loadWeightValue()
    }

    Scaffold(topBar = {
        TopBar(title = stringResource(id = R.string.animal_detail),
            onBackClick = { navController.popBackStack() })
    }) { innerPadding ->
        if (isLoading) {
            IsLoading()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
            ) {
                AnimalInfo(
                    navController, animalDetail, lotId, age, weights.first().weight * weightValue
                )
                SectionWithLazyRow(titleRes = R.string.vaccines,
                    items = vaccines,
                    cardContent = { VaccineCard(it) },
                    addActionTextRes = R.string.add_vaccine,
                    onClickAdd = { /*TODO*/ })
                SectionWithLazyRow(titleRes = R.string.events,
                    items = events,
                    cardContent = { EventCard(it) },
                    addActionTextRes = R.string.add_event,
                    onClickAdd = { /*TODO*/ })
                AnimalWeights(weights = weights, onClickAdd = { /*TODO*/ })
                OutlinedButton(
                    onClick = {
                        viewModel.deleteAnimal(animalId)
                        navController.navigate(AnimalsNav) {
                            popUpTo(AnimalDetailNav) { inclusive = true }
                        }
                    },
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Red),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.delete_animal), color = Red
                    )
                }
            }
        }
    }
}

@Composable
fun AnimalInfo(
    navController: NavHostController,
    animalDetail: AnimalDetail,
    lotId: Int,
    age: Triple<Int, Int, Int>,
    approxSale: Float,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val genderIcon: Int = if (animalDetail.gender == Gender.Male) {
                    R.drawable.ic_bull
                } else {
                    R.drawable.ic_cow
                }
                Image(
                    painter = painterResource(id = genderIcon),
                    contentDescription = stringResource(R.string.animal_icon),
                    modifier = Modifier.size(56.dp),
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ID: ${animalDetail.id}", style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "TAG: ${animalDetail.tag}",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedButton(
                    onClick = {
                        navController.navigate(
                            AnimalFormNav(animalDetail.id, animalDetail.tag)
                        )
                    },
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, LightBlue),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.edit_animal), color = LightBlue
                    )
                }
            }

            InfoRowWithClickableLot(navController, lotId, animalDetail)

            InfoRow(
                titleRes = R.string.birth_date,
                value = animalDetail.birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                titleRes2 = R.string.age,
                value2 = when {
                    age.first > 0 -> "${age.first} " + stringResource(id = R.string.years)
                    age.second > 0 -> "${age.second} " + stringResource(id = R.string.months)
                    else -> "${age.third} " + stringResource(id = R.string.days)
                }
            )

            if (animalDetail.state == State.Sold) {
                InfoRow(
                    titleRes = R.string.purchase_value,
                    value = "$" + formatNumber(animalDetail.purchaseValue.toString()),
                    titleRes2 = R.string.sale_value,
                    value2 = "$" + formatNumber(animalDetail.saleValue.toString())
                )
            } else {
                InfoRow(
                    titleRes = R.string.purchase_value,
                    value = "$" + formatNumber(animalDetail.purchaseValue.toString()),
                    titleRes2 = R.string.approximate_purchase_value,
                    value2 = "$" + formatNumber(approxSale.toString())
                )
            }
        }
    }
}

@Composable
fun InfoRowWithClickableLot(
    navController: NavHostController, lotId: Int, animalDetail: AnimalDetail
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .background(
                color = if (lotId != 0) Green.copy(alpha = 0.2f) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = lotId != 0) {
                if (lotId != 0) navController.navigate(LotDetailNav(lotId))
            }
            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 32.dp)) {
            Text(
                text = stringResource(id = R.string.lot),
                style = MaterialTheme.typography.bodySmall,
                color = LightGray
            )
            Text(
                text = if (lotId == 0) "ND" else lotId.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.state),
                style = MaterialTheme.typography.bodySmall,
                color = LightGray
            )
            Text(
                text = stringResource(
                    id = when (animalDetail.state) {
                        State.Healthy -> R.string.healthy
                        State.Sick -> R.string.sick
                        State.Injured -> R.string.injured
                        State.Dead -> R.string.dead
                        State.Sold -> R.string.sold
                    }
                ), style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun InfoRow(
    titleRes: Int, value: String, titleRes2: Int, value2: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = titleRes),
                style = MaterialTheme.typography.bodySmall,
                color = LightGray
            )
            Text(
                text = value, style = MaterialTheme.typography.bodyMedium
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = titleRes2),
                style = MaterialTheme.typography.bodySmall,
                color = LightGray
            )
            Text(
                text = value2, style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun AddButton(text: Int, onClick: () -> Unit) {
    OutlinedButton(
        onClick = { onClick() },
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, Green),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent
        )
    ) {
        Text(
            text = stringResource(id = text), color = Green
        )
    }
}

@Composable
fun SectionWithLazyRow(
    onClickAdd: () -> Unit,
    titleRes: Int,
    items: List<Description>,
    cardContent: @Composable (Description) -> Unit,
    addActionTextRes: Int
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = titleRes),
                style = MaterialTheme.typography.titleSmall,
            )
            AddButton(text = addActionTextRes, onClick = onClickAdd)
        }
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
            items(items.size) { index ->
                cardContent(items[index])
            }
        }
    }
}

@Composable
fun VaccineCard(vaccine: Description) {
    Card(
        modifier = Modifier
            .width(256.dp)
            .padding(end = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = vaccine.title, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = vaccine.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                style = MaterialTheme.typography.bodySmall,
                color = LightGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = vaccine.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun EventCard(event: Description) {
    Card(
        modifier = Modifier
            .width(256.dp)
            .padding(end = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = event.title, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = event.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                style = MaterialTheme.typography.bodySmall,
                color = LightGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = event.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun AnimalWeights(weights: List<Weight>, onClickAdd: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.weights),
                style = MaterialTheme.typography.titleSmall
            )
            AddButton(text = R.string.add_weight, onClick = onClickAdd)
        }
        if (weights.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
            ) {
                weights.forEach { weight ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = weight.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${weight.weight} kg",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}