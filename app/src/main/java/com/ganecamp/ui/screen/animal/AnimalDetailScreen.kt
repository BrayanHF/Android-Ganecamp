package com.ganecamp.ui.screen.animal

import androidx.compose.foundation.Image
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
import com.ganecamp.domain.enums.AnimalGender
import com.ganecamp.domain.enums.AnimalState
import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.enums.RepositoryRespond
import com.ganecamp.ui.component.bar.GenericTopBar
import com.ganecamp.ui.component.button.ActionButton
import com.ganecamp.ui.component.card.InfoCard
import com.ganecamp.ui.component.dialog.RepositoryErrorDialog
import com.ganecamp.ui.component.layout.SectionWithLazyColumn
import com.ganecamp.ui.component.layout.SectionWithLazyRow
import com.ganecamp.ui.component.layout.TwoColumns
import com.ganecamp.ui.component.misc.IsLoading
import com.ganecamp.ui.navigation.screens.AnimalFormNav
import com.ganecamp.ui.navigation.screens.EventAddFormNav
import com.ganecamp.ui.navigation.screens.LotDetailNav
import com.ganecamp.ui.navigation.screens.VaccineAddFormNav
import com.ganecamp.ui.navigation.screens.WeightFormNav
import com.ganecamp.ui.theme.LightBlue
import com.ganecamp.ui.theme.LightGray
import com.ganecamp.ui.theme.Red
import com.ganecamp.ui.util.TimeUtil
import com.ganecamp.ui.util.formatNumber
import com.ganecamp.ui.util.getAnimalBreedRes
import com.ganecamp.ui.util.getAnimalStateRes

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
        GenericTopBar(
            onBackClick = { navController.popBackStack() },
            title = stringResource(id = R.string.animal_detail),
        )
    }) { innerPadding ->
        if (isLoading) {
            IsLoading()
        } else {
            animal?.let { animal ->
                val approxSold = if (weights.isNotEmpty() && weightValue != null) {
                    weightValue!!.value * weights.first().weight
                } else {
                    0f
                }
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    AnimalDetailContent(
                        navController = navController,
                        animal = animal,
                        vaccines = vaccines,
                        events = events,
                        weights = weights,
                        age = age,
                        approxSold = approxSold,
                    )
                    ActionButton(
                        text = stringResource(R.string.delete_animal),
                        onClick = {
                            animal.id?.let { animalId ->
                                viewModel.deleteAnimal(animalId)
                                navController.popBackStack()
                            }
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
fun AnimalDetailContent(
    navController: NavHostController,
    animal: Animal,
    vaccines: List<VaccineApplied>,
    events: List<EventApplied>,
    weights: List<Weight>,
    age: Triple<Int, Int, Int>?,
    approxSold: Float,
) {
    AnimalInfo(
        navController, animal, age, approxSold
    )

    SectionWithLazyRow(titleSection = stringResource(R.string.vaccines),
        itemsSection = vaccines,
        cardItem = { vaccine ->
            InfoCard(onClick = { //Todo: Add navigation to vaccine detail
            }, info = vaccine.name, date = vaccine.date.toInstant())
        },
        textButtonAdd = stringResource(R.string.add_vaccine),
        onClickAdd = {
            animal.id?.let { animalId ->
                navController.navigate(VaccineAddFormNav(animalId))
            }
        })

    SectionWithLazyRow(titleSection = stringResource(R.string.events),
        itemsSection = events,
        cardItem = { event ->
            InfoCard(onClick = { //Todo: Add navigation to event detail
            }, info = event.title, date = event.date.toInstant())
        },
        textButtonAdd = stringResource(R.string.add_event),
        onClickAdd = {
            animal.id?.let { animalId ->
                navController.navigate(EventAddFormNav(animalId, EntityType.Animal))
            }
        })

    SectionWithLazyColumn(titleSection = stringResource(R.string.weights),
        itemsSection = weights,
        cardItem = { weight ->
            WeightCard(weight = weight, onClick = { //Todo: Add navigation to weight detail
            })
        },
        textButtonAdd = stringResource(R.string.add_weight),
        onClickAdd = {
            animal.id?.let { animalId ->
                navController.navigate(WeightFormNav(animalId))
            }
        })

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

            val (stateTextRes, _) = getAnimalStateRes(animal.animalState)
            TwoColumns(titleLeft = stringResource(id = R.string.lot),
                valueLeft = animal.lotId ?: stringResource(R.string.nothing),
                titleRight = stringResource(id = R.string.state),
                valueRight = stringResource(id = stateTextRes),
                isClickableLeft = animal.lotId != null,
                onClickLeft = {
                    animal.lotId?.let { lotId ->
                        navController.navigate(LotDetailNav(lotId))
                    }
                })

            if (age != null) {
                TwoColumns(
                    titleLeft = stringResource(id = R.string.birth_date),
                    valueLeft = TimeUtil.formatter.format(animal.birthDate.toInstant()),
                    titleRight = stringResource(R.string.age),
                    valueRight = when {
                        age.first > 0 -> "${age.first} " + stringResource(id = R.string.years)
                        age.second > 0 -> "${age.second} " + stringResource(id = R.string.months)
                        else -> "${age.third} " + stringResource(id = R.string.days)
                    }
                )
            }

            TwoColumns(
                titleLeft = stringResource(id = R.string.purchase_date),
                valueLeft = TimeUtil.formatter.format(animal.purchaseDate.toInstant()),
                titleRight = stringResource(R.string.purchase_value),
                valueRight = "$" + formatNumber(animal.purchaseValue.toString())
            )

            if (animal.animalState == AnimalState.Sold) {
                TwoColumns(
                    titleLeft = stringResource(R.string.sale_date),
                    valueLeft = TimeUtil.formatter.format(animal.saleDate.toInstant()),
                    titleRight = stringResource(R.string.sale_value),
                    valueRight = "$" + formatNumber(animal.saleValue.toString())
                )
            }

            val titleRight: String
            val valueRight: String
            if (animal.animalState != AnimalState.Sold) {
                titleRight = stringResource(R.string.approximate_purchase_value)
                valueRight = "$" + formatNumber(approxSold.toString())
            } else {
                val difference = animal.saleValue - animal.purchaseValue

                valueRight = "$" + formatNumber(difference.toString())
                titleRight = if (difference > 0) {
                    stringResource(R.string.profit)
                } else {
                    stringResource(R.string.loss)
                }
            }

            TwoColumns(
                titleLeft = stringResource(R.string.breed),
                valueLeft = stringResource(id = getAnimalBreedRes(animal.animalBreed)),
                titleRight = titleRight,
                valueRight = valueRight
            )
        }
    }
}

@Composable
fun WeightCard(weight: Weight, onClick: () -> Unit?) {
    Card(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
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