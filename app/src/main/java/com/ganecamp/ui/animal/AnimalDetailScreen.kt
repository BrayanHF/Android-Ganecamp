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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ganecamp.R
import com.ganecamp.model.objects.Animal
import com.ganecamp.model.objects.EventApplied
import com.ganecamp.model.objects.VaccineApplied
import com.ganecamp.model.objects.Weight
import com.ganecamp.ui.general.BarColor
import com.ganecamp.ui.general.IsLoading
import com.ganecamp.ui.general.ShowFirestoreError
import com.ganecamp.ui.general.TopBar
import com.ganecamp.ui.general.formatNumber
import com.ganecamp.ui.navigation.AnimalDetailNav
import com.ganecamp.ui.navigation.AnimalFormNav
import com.ganecamp.ui.navigation.AnimalsNav
import com.ganecamp.ui.navigation.LotDetailNav
import com.ganecamp.ui.theme.Green
import com.ganecamp.ui.theme.LightBlue
import com.ganecamp.ui.theme.LightGray
import com.ganecamp.ui.theme.LightGreen
import com.ganecamp.ui.theme.Red
import com.ganecamp.utilities.enums.FirestoreRespond
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State
import java.time.format.DateTimeFormatter

@Composable
fun AnimalDetailScreen(navController: NavHostController, animalId: String?) {
    val viewModel: AnimalDetailViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading.collectAsState(initial = true)
    val animal: Animal? by viewModel.animal.collectAsState()
    val vaccines: List<VaccineApplied> by viewModel.vaccines.collectAsState()
    val events: List<EventApplied> by viewModel.events.collectAsState()
    val weights: List<Weight> by viewModel.weights.collectAsState()
    val age by viewModel.ageAnimal.collectAsState()
    val weightValue by viewModel.weightValue.collectAsState()
    val error by viewModel.error.collectAsState()

    BarColor(LightGreen)

    LaunchedEffect(animalId) {
        if (animalId != null) {
            viewModel.loadAnimal(animalId)
            viewModel.loadVaccines(animalId)
            viewModel.loadEvents(animalId)
            viewModel.loadWeights(animalId)
            viewModel.loadWeightValue()
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

                val approxSold = if (weights.isNotEmpty() && weightValue != null) {
                    weightValue!!.value * weights.first().weight
                } else {
                    0f
                }

                AnimalInfo(
                    navController, animal!!, age, approxSold.toString()
                )
                SectionWithLazyRow(titleRes = R.string.vaccines,
                    items = vaccines,
                    cardContent = { VaccineCard(it) },
                    addActionTextRes = R.string.add_vaccine,
                    onClickAdd = { /*Todo: All the vaccines screen and here the navigation*/ })
                SectionWithLazyRow(titleRes = R.string.events,
                    items = events,
                    cardContent = { EventCard(it) },
                    addActionTextRes = R.string.add_event,
                    onClickAdd = { /*Todo: All the events screen and here the navigation*/ })
                AnimalWeights(
                    weights = weights,
                    onClickAdd = { /*Todo: All the weights screen and here the navigation*/ })
                OutlinedButton(
                    onClick = {
                        viewModel.deleteAnimal(animal!!.tag)
                        navController.navigate(AnimalsNav) {
                            popUpTo(AnimalDetailNav(animalId)) { inclusive = true }
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

//Todo: Fix the dates and the number format
@Composable
fun AnimalInfo(
    navController: NavHostController,
    animal: Animal,
    age: Triple<Int, Int, Int>?,
    approxSold: String
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
                val genderIcon: Int = if (animal.gender == Gender.Male) {
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
                        text = "ID: ${animal.id}", style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "TAG: ${animal.tag}", style = MaterialTheme.typography.titleSmall
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedButton(
                    onClick = {
                        navController.navigate(
                            AnimalFormNav(animal.id!!, animal.tag)
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

            InfoRowWithClickableLot(navController, animal)

            if (age != null) {
                InfoRow(
                    titleRes = R.string.birth_date,
                    value = animal.birthDate.toString()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    titleRes2 = R.string.age,
                    value2 = when {
                        age.first > 0 -> "${age.first} " + stringResource(id = R.string.years)
                        age.second > 0 -> "${age.second} " + stringResource(id = R.string.months)
                        else -> "${age.third} " + stringResource(id = R.string.days)
                    }
                )
            }

            if (animal.state == State.Sold) {
                InfoRow(
                    titleRes = R.string.purchase_value,
                    value = "$" + formatNumber(animal.purchaseValue.toString()),
                    titleRes2 = R.string.sale_value,
                    value2 = "$" + formatNumber(animal.saleValue.toString())
                )
            } else {
                InfoRow(
                    titleRes = R.string.purchase_value,
                    value = "$" + formatNumber(animal.purchaseValue.toString()),
                    titleRes2 = R.string.approximate_purchase_value,
                    value2 = "$" + formatNumber(approxSold)
                )
            }
        }
    }
}

@Composable
fun InfoRowWithClickableLot(
    navController: NavHostController, animal: Animal
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .background(
                color = if (animal.lotId != null) Green.copy(alpha = 0.2f) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = animal.lotId != null) {
                if (animal.lotId != null) navController.navigate(LotDetailNav(animal.lotId))
            }
            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 32.dp)) {
            Text(
                text = stringResource(id = R.string.lot),
                style = MaterialTheme.typography.bodySmall,
                color = LightGray
            )
            Text(
                text = animal.lotId ?: stringResource(id = R.string.nothing),
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
                    id = when (animal.state) {
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
fun <Applied> SectionWithLazyRow(
    onClickAdd: () -> Unit,
    titleRes: Int,
    items: List<Applied>,
    cardContent: @Composable (Applied) -> Unit,
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

//Todo: Clickable card with navigation to the detail of the vaccine
@Composable
fun VaccineCard(vaccine: VaccineApplied) {
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
            Text(text = vaccine.name, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = vaccine.date.toString().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                style = MaterialTheme.typography.bodySmall,
                color = LightGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = vaccine.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}

//Todo: Clickable card with navigation to the detail of the event
@Composable
fun EventCard(event: EventApplied) {
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
                text = event.date.toString().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                style = MaterialTheme.typography.bodySmall,
                color = LightGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = event.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}

//Todo: Clickable card with navigation to the detail of the weight
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
                            text = weight.date.toString()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
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