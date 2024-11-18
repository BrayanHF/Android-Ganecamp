package com.ganecamp.ui.screen.animal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.ganecamp.data.firibase.model.Animal
import com.ganecamp.data.firibase.model.EventApplied
import com.ganecamp.data.firibase.model.VaccineApplied
import com.ganecamp.data.firibase.model.Weight
import com.ganecamp.ui.component.bar.GeneralTopBar
import com.ganecamp.ui.component.button.ActionButton
import com.ganecamp.ui.component.dialog.RepositoryErrorDialog
import com.ganecamp.ui.component.misc.IsLoading
import com.ganecamp.ui.component.misc.SectionWithLazyRow

import com.ganecamp.ui.navigation.screens.AnimalDetailNav
import com.ganecamp.ui.navigation.screens.AnimalFormNav
import com.ganecamp.ui.navigation.screens.AnimalsNav
import com.ganecamp.ui.navigation.screens.EventAddFormNav
import com.ganecamp.ui.navigation.screens.LotDetailNav
import com.ganecamp.ui.navigation.screens.VaccineAddFormNav
import com.ganecamp.ui.navigation.screens.WeightFormNav
import com.ganecamp.ui.theme.Green
import com.ganecamp.ui.theme.LightBlue
import com.ganecamp.ui.theme.LightGray
import com.ganecamp.ui.theme.Red
import com.ganecamp.ui.util.formatNumber
import com.ganecamp.ui.util.getAnimalBreedRes
import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.enums.AnimalGender
import com.ganecamp.domain.enums.RepositoryRespond
import com.ganecamp.domain.enums.AnimalState
import com.ganecamp.ui.util.TimeUtil

@Composable
fun AnimalDetailScreen(navController: NavHostController, animalId: String) {
    val viewModel: AnimalDetailViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading.collectAsState(initial = true)
    val animal: Animal? by viewModel.animal.collectAsState()
    val vaccines: List<VaccineApplied> by viewModel.vaccines.collectAsState()
    val events: List<EventApplied> by viewModel.events.collectAsState()
    val weights: List<Weight> by viewModel.weights.collectAsState()
    val age by viewModel.ageAnimal.collectAsState()
    val weightValue by viewModel.weightValue.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(animalId) {
        viewModel.loadAnimal(animalId)
        viewModel.loadVaccines(animalId)
        viewModel.loadEvents(animalId)
        viewModel.loadWeights(animalId)
        viewModel.loadWeightValue()
    }

    var showError by remember { mutableStateOf(false) }
    LaunchedEffect(error) {
        if (error != RepositoryRespond.OK) {
            showError = true
        }
    }

    if (showError) {
        RepositoryErrorDialog(error = error, onDismiss = { showError = false })
    }

    Scaffold(topBar = {
        GeneralTopBar(title = stringResource(id = R.string.animal_detail),
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
                    navController, animal!!, age, approxSold
                )
                SectionWithLazyRow(titleSection = stringResource(R.string.vaccines),
                    itemsSection = vaccines,
                    cardItem = { VaccineCard(it) },
                    textButtonAdd = stringResource(R.string.add_vaccine),
                    onClickAdd = {
                        animal?.id?.let { animalId ->
                            navController.navigate(VaccineAddFormNav(animalId))
                        }
                    })
                SectionWithLazyRow(titleSection = stringResource(R.string.events),
                    itemsSection = events,
                    cardItem = { EventCard(it) },
                    textButtonAdd = stringResource(R.string.add_event),
                    onClickAdd = {
                        animal?.id?.let { animalId ->
                            navController.navigate(EventAddFormNav(animalId, EntityType.Animal))
                        }
                    })
                AnimalWeights(weights = weights, onClickAdd = {
                    animal?.id?.let { animalId ->
                        navController.navigate(WeightFormNav(animalId))
                    }
                })

                ActionButton(text = stringResource(R.string.delete_animal), color = Red, onClick = {
                    viewModel.deleteAnimal(animal!!.tag)
                    navController.navigate(AnimalsNav) {
                        popUpTo(AnimalDetailNav(animalId)) { inclusive = true }
                    }
                })
            }
        }
    }
}

@Composable
fun AnimalInfo(
    navController: NavHostController, animal: Animal, age: Triple<Int, Int, Int>?, approxSold: Float
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
            Text(
                text = "TAG: ${animal.tag}",
                style = MaterialTheme.typography.titleSmall,
                color = LightGray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val genderIcon: Int
                val genderText: Int
                if (animal.animalGender == AnimalGender.Male) {
                    genderIcon = R.drawable.ic_bull
                    genderText = R.string.male
                } else {
                    genderIcon = R.drawable.ic_cow
                    genderText = R.string.female
                }
                Image(
                    painter = painterResource(id = genderIcon),
                    contentDescription = stringResource(R.string.animal_icon),
                    modifier = Modifier.size(56.dp),
                )
                Text(
                    text = stringResource(id = genderText),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.width(16.dp))
                ActionButton(
                    text = stringResource(id = R.string.edit_animal),
                    color = LightBlue,
                    onClick = {
                        navController.navigate(AnimalFormNav(animal.id, animal.tag))
                    })
            }

            InfoRowWithClickableLot(navController, animal)

            if (age != null) {
                InfoRow(
                    titleRes = R.string.birth_date,
                    value = TimeUtil.formatter.format(animal.birthDate.toInstant()),
                    titleRes2 = R.string.age,
                    value2 = when {
                        age.first > 0 -> "${age.first} " + stringResource(id = R.string.years)
                        age.second > 0 -> "${age.second} " + stringResource(id = R.string.months)
                        else -> "${age.third} " + stringResource(id = R.string.days)
                    }
                )
            }

            InfoRow(
                titleRes = R.string.purchase_date,
                value = TimeUtil.formatter.format(animal.purchaseDate.toInstant()),
                titleRes2 = R.string.purchase_value,
                value2 = "$" + formatNumber(animal.purchaseValue.toString())
            )



            if (animal.animalState == AnimalState.Sold) {
                InfoRow(
                    titleRes = R.string.sale_date,
                    value = TimeUtil.formatter.format(animal.saleDate.toInstant()),
                    titleRes2 = R.string.sale_value,
                    value2 = "$" + formatNumber(animal.saleValue.toString())
                )
            }

            val titleRes2: Int
            val value2: String

            if (animal.animalState != AnimalState.Sold) {
                titleRes2 = R.string.approximate_purchase_value
                value2 = "$" + formatNumber(approxSold.toString())
            } else {
                val difference = animal.saleValue - animal.purchaseValue

                value2 = "$" + formatNumber(difference.toString())
                titleRes2 = if (difference > 0) {
                    R.string.profit
                } else {
                    R.string.loss
                }
            }

            InfoRow(
                titleRes = R.string.breed,
                value = stringResource(id = getAnimalBreedRes(animal.animalBreed)),
                titleRes2 = titleRes2,
                value2 = value2
            )

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
                    id = when (animal.animalState) {
                        AnimalState.Healthy -> R.string.healthy
                        AnimalState.Sick -> R.string.sick
                        AnimalState.Injured -> R.string.injured
                        AnimalState.Dead -> R.string.dead
                        AnimalState.Sold -> R.string.sold
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
                text = TimeUtil.formatter.format(vaccine.date.toInstant()),
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
                text = TimeUtil.formatter.format(event.date.toInstant()),
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
            ActionButton(
                text = stringResource(R.string.add_weight), color = Green, onClick = onClickAdd
            )
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
                            text = TimeUtil.formatter.format(weight.date.toInstant()),
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