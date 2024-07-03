package com.ganecamp.ui.animals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.navigation.NavHostController
import com.ganecamp.R
import com.ganecamp.domain.model.AnimalDetail
import com.ganecamp.domain.model.Description
import com.ganecamp.domain.model.Weight
import com.ganecamp.ui.general.GeneralBox
import com.ganecamp.ui.general.GeneralDescriptionCard
import com.ganecamp.ui.general.IsLoading
import com.ganecamp.ui.theme.LightGreenAlpha
import com.ganecamp.ui.theme.Typography
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AnimalDetailScreen(navHostController: NavHostController, animalId: Int, lotId: Int) {
    val viewModel: AnimalDetailViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading.observeAsState(initial = true)
    val animalDetail: AnimalDetail by viewModel.animal.observeAsState(
        initial = AnimalDetail(
            "", animalId, Gender.Male, ZonedDateTime.now(), 0.0, 0.0, State.Healthy
        )
    )
    val vaccines: List<Description> by viewModel.vaccines.observeAsState(initial = emptyList())
    val events: List<Description> by viewModel.events.observeAsState(initial = emptyList())
    val weights: List<Weight> by viewModel.weights.observeAsState(initial = emptyList())
    val age by viewModel.ageAnimal.observeAsState(initial = Triple(0, 0, 0))

    GeneralBox {
        if (isLoading) {
            IsLoading()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                AnimalInfo(navHostController, animalDetail, lotId, age)
                if (vaccines.isNotEmpty()) {
                    AnimalVaccines(vaccines)
                }
                if (events.isNotEmpty()) {
                    AnimalEvents(events)
                }
                if (weights.isNotEmpty()) {
                    AnimalWeights(weights)
                }
            }
        }
    }
}

@Composable
fun AnimalDetailTopBarContent() {
    Text(text = stringResource(id = R.string.animal_detail))
}

@Composable
fun AnimalInfo(
    navHostController: NavHostController,
    animalDetail: AnimalDetail,
    lotId: Int,
    age: Triple<Int, Int, Int>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val icGender: Int = if (animalDetail.gender == Gender.Male) {
                R.drawable.ic_bull
            } else {
                R.drawable.ic_cow
            }
            Icon(
                painter = painterResource(id = icGender),
                contentDescription = stringResource(R.string.animal_icon),
                modifier = Modifier.size(128.dp)
            )
        }
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "TAG: ${animalDetail.tag}",
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "ID: ${animalDetail.id}",
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
                val textGender: Int = if (animalDetail.gender == Gender.Male) {
                    R.string.male
                } else {
                    R.string.female
                }
                Text(
                    text = stringResource(id = textGender),
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val textState = when (animalDetail.state) {
                    State.Healthy -> R.string.healthy
                    State.Sick -> R.string.sick
                    State.Injured -> R.string.injured
                    State.Dead -> R.string.dead
                    State.Sold -> R.string.sold
                }
                Text(
                    text = stringResource(id = textState),
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )

                Surface(
                    onClick = { if (lotId != 0) navHostController.navigate("lotDetail/$lotId") },
                    shape = RoundedCornerShape(12.dp),
                    color = LightGreenAlpha
                ) {
                    val textLot = if (lotId == 0) {
                        "ND"
                    } else {
                        lotId
                    }
                    Text(
                        text = stringResource(id = R.string.lot) + ": $textLot",
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            Text(
                text = stringResource(id = R.string.birth_date) + animalDetail.birthDate.format(
                    formatter
                ), style = Typography.bodyMedium, modifier = Modifier.padding(8.dp)
            )

        }
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Surface {
                Text(
                    text = stringResource(id = R.string.age) + ":",
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
            if (age.first != 0) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.years),
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp)
                    )
                    Text(
                        text = age.first.toString(),
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)
                    )
                }
            }

            if (age.second != 0) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.months),
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp)
                    )
                    Text(
                        text = age.second.toString(),
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)
                    )
                }
            }

            if (age.third != 0) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.days),
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp)
                    )
                    Text(
                        text = age.third.toString(),
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)
                    )
                }
            }
        }
    }


    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.buy) + ":",
                style = Typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = animalDetail.purchaseValue.toString(),
                style = Typography.bodyMedium,
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.sale) + ":",
                style = Typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = animalDetail.saleValue.toString(),
                style = Typography.bodyMedium,
            )
        }
    }
}

@Composable
fun AnimalVaccines(vaccines: List<Description>) {
    Text(
        text = "Vaccines",
        style = Typography.bodyMedium,
        modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp)
    )
    LazyRow {
        items(vaccines.size) {
            GeneralDescriptionCard(description = vaccines[it])
        }

    }
}

@Composable
fun AnimalEvents(events: List<Description>) {
    Text(
        text = "Events",
        style = Typography.bodyMedium,
        modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp)
    )
    LazyRow {
        items(events.size) {
            GeneralDescriptionCard(description = events[it])
        }
    }
}

@Composable
fun AnimalWeights(weights: List<Weight>) {
    Text(
        text = "Pesos",
        style = Typography.bodyMedium,
        modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp)
    )
    weights.forEach {
        WeightRow(it)
        if (it != weights.last()) {
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        }
    }
}

@Composable
fun WeightRow(weight: Weight) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "${weight.weight} kg",
            modifier = Modifier.weight(1f),
            style = Typography.bodyMedium
        )
        Text(
            text = weight.date.format(formatter),
            modifier = Modifier.weight(1f),
            style = Typography.bodyMedium
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = stringResource(R.string.edit),
            modifier = Modifier.size(24.dp)
        )
    }
}