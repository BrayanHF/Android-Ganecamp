package com.ganecamp.ui.animals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ganecamp.domain.model.Animal
import com.ganecamp.R
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State
import com.ganecamp.ui.theme.*


@Composable
fun AnimalScreen(navController: NavHostController) {
    val viewModel: AnimalsViewModel = hiltViewModel()
    val animals by viewModel.animals.observeAsState(initial = emptyList())
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = true)

    Box(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if (isLoading) {
            IsLoading()
        } else {
            AnimalList(navController, animals)
        }
    }
}

@Composable
fun AnimalList(navController: NavHostController, animals: List<Animal>) {
    if (animals.isEmpty()) {
        Column {
            Text(
                text = stringResource(id = R.string.no_animals),
                style = Typography.bodyLarge
            )
        }
    } else {
        LazyColumn(verticalArrangement = Arrangement.Top) {
            items(animals) { animal ->
                AnimalItem(navController, animal)
            }
        }
    }
}

@Composable
fun IsLoading() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun AnimalItem(navController: NavHostController, animal: Animal) {
    Surface(
//        tonalElevation = 16.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(0.dp, 4.dp)
            .fillMaxSize(),
        shadowElevation = 4.dp,
        color = White
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreenAlpha)
        ) {
            val (iconGender, idText, lotText, stateText, bottomLine) = createRefs()

            val guideline1 = createGuidelineFromStart(0.24f)
            val guideline2 = createGuidelineFromStart(0.62f)

            val icGender: Int
            val textGender: Int
            if (animal.gender == Gender.Male) {
                icGender = R.drawable.ic_bull
                textGender = R.string.male
            } else {
                icGender = R.drawable.ic_cow
                textGender = R.string.female
            }

            Column(modifier = Modifier
                .constrainAs(iconGender) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
                .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Icon(
                    painter = painterResource(id = icGender),
                    contentDescription = stringResource(R.string.animal_icon),
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    text = stringResource(
                        id = textGender
                    ),
                    style = Typography.bodySmall
                )
            }

            Column(
                modifier = Modifier.constrainAs(idText) {
                    top.linkTo(parent.top)
                    start.linkTo(guideline1)
                    bottom.linkTo(stateText.top)
                    end.linkTo(guideline2)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }, verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "ID: ${animal.id}",
                    style = Typography.titleSmall
                )
            }

            Column(
                modifier = Modifier.constrainAs(lotText) {
                    top.linkTo(parent.top)
                    start.linkTo(guideline2)
                    bottom.linkTo(stateText.top)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }, verticalArrangement = Arrangement.Center
            ) {
                if (animal.lotId != 0) {
                    Text(
                        text = "LT: ${animal.lotId}",
                        style = Typography.bodyMedium
                    )
                }
            }

            val textState: Int
            val colorState: Color

            when (animal.state) {
                State.Healthy -> {
                    textState = R.string.healthy
                    colorState = Green
                }

                State.Sick -> {
                    textState = R.string.sick
                    colorState = Orange
                }

                State.Injured -> {
                    textState = R.string.injured
                    colorState = Yellow
                }

                State.Dead -> {
                    textState = R.string.dead
                    colorState = Black
                }

                State.Sold -> {
                    textState = R.string.sold
                    colorState = LightGreenAlpha
                }
            }

            Column(
                modifier = Modifier.constrainAs(stateText) {
                    top.linkTo(idText.bottom)
                    start.linkTo(iconGender.end)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }, verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = textState),
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = Typography.bodyLarge,
                    textAlign = TextAlign.Center

                )
            }

            Box(modifier = Modifier
                .constrainAs(bottomLine) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(4.dp)
                }
                .background(colorState)
            )
        }
    }
}
