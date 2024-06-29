package com.ganecamp.ui.animals

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganecamp.R
import com.ganecamp.domain.model.AnimalDetail
import com.ganecamp.domain.model.Description
import com.ganecamp.domain.model.Weight
import com.ganecamp.ui.general.GeneralBox
import com.ganecamp.ui.theme.LightGreenAlpha
import com.ganecamp.ui.theme.Typography
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


// TODO: parameters: NavHostController, animalId, lotId
@Preview
@Composable
fun AnimalDetailScreen() {

    val animalDetail =
        AnimalDetail("1", 1, Gender.Female, ZonedDateTime.now(), 0.0, 0.0, State.Healthy)
    val lotId = 1

    GeneralBox {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            AnimalInfo(animalDetail, lotId)
//            AnimalVaccines()
//            AnimalEvents()
            AnimalWeights()
        }
    }
}

@Composable
fun AnimalInfo(animalDetail: AnimalDetail, lotId: Int) {
    val edad: Triple<Int, Int, Int> = Triple(0, 2, 1)
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
                    onClick = { /* TODO: Go to lot detail */ },
                    shape = RoundedCornerShape(12.dp),
                    color = LightGreenAlpha
                ) {
                    Text(
                        text = "Lote: $lotId",
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
            if (edad.first != 0) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.years),
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp)
                    )
                    Text(
                        text = edad.first.toString(),
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)
                    )
                }
            }

            if (edad.second != 0) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.months),
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp)
                    )
                    Text(
                        text = edad.second.toString(),
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)
                    )
                }
            }

            if (edad.third != 0) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.days),
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp)
                    )
                    Text(
                        text = edad.third.toString(),
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
                text = "Compra:", style = Typography.bodyMedium, modifier = Modifier.padding(8.dp)
            )
            Text(
                text = animalDetail.purchaseValue.toString(),
                style = Typography.bodyMedium,
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Venta", style = Typography.bodyMedium, modifier = Modifier.padding(8.dp)
            )
            Text(
                text = animalDetail.saleValue.toString(),
                style = Typography.bodyMedium,
            )
        }


    }
}

@Composable
fun AnimalVaccines(
//    vaccines: List<Description>
) {
    Text(
        text = "Vaccines",
        style = Typography.bodyMedium,
        modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp)
    )
    val descriptions = listOf(
        Description("Vacuna 1", ZonedDateTime.now(), "Descripción 1"),
        Description("Vacuna 1", ZonedDateTime.now(), "Descripción 1")
    )
    LazyRow {
        items(descriptions.size) {
            DescriptionCard(description = descriptions[it])
        }

    }
}

@Composable
fun AnimalEvents(
//    events: List<Description>
) {
    Text(
        text = "Events",
        style = Typography.bodyMedium,
        modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp)
    )
    val descriptions = listOf(
        Description("Event 1", ZonedDateTime.now(), "Descripción 1"),
        Description("Event 1", ZonedDateTime.now(), "Descripción 1")
    )
    LazyRow {
        items(descriptions.size) {
            DescriptionCard(description = descriptions[it])
        }
    }
}

@Composable
fun AnimalWeights() {
    Text(
        text = "Pesos",
        style = Typography.bodyMedium,
        modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp)
    )
    val weights = listOf(
        Weight(1, 1, 100.0, ZonedDateTime.now()),
        Weight(2, 1, 200.0, ZonedDateTime.now()),
        Weight(3, 1, 300.0, ZonedDateTime.now())
    )
    LazyColumn() {
        items(weights.size) {
            WeightRow(weight = weights[it])
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        }
    }
}

@Composable
fun DescriptionCard(description: Description) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val formattedDate = description.date.format(formatter)

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        modifier = Modifier
            .padding(8.dp)
            .width(300.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = description.title, style = Typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = formattedDate, style = Typography.titleSmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description.description, style = Typography.titleSmall)
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