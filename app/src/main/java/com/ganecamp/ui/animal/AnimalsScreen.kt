package com.ganecamp.ui.animal

import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ganecamp.R
import com.ganecamp.domain.model.Animal
import com.ganecamp.ui.general.IsLoading
import com.ganecamp.ui.general.NoRegistered
import com.ganecamp.ui.general.getAnimalStateInfo
import com.ganecamp.ui.navigation.AnimalDetailNav
import com.ganecamp.utilities.enums.Gender

@Composable
fun AnimalScreen(navController: NavHostController) {
    val viewModel: AnimalsViewModel = hiltViewModel()
    val animals by viewModel.animals.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(true)

    LaunchedEffect(navController.currentBackStackEntry) {
        viewModel.loadAnimals()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 0.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            isLoading -> IsLoading()
            animals.isNotEmpty() -> AnimalList(navController, animals)
            else -> NoRegistered(textId = R.string.no_animals)
        }
    }
}

@Composable
fun AnimalList(navController: NavHostController, animals: List<Animal>) {
    val columns =
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(animals) { animal ->
            AnimalCard(navController, animal)
        }
    }
}

@Composable
fun AnimalCard(navController: NavHostController, animal: Animal) {
    val genderIcon: Int = if (animal.gender == Gender.Male) {
        R.drawable.ic_bull
    } else {
        R.drawable.ic_cow
    }
    val animalStateInfo = getAnimalStateInfo(animal.state)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { navController.navigate(AnimalDetailNav(animal.id)) },
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = genderIcon),
                contentDescription = stringResource(R.string.animal_icon),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "ID: ${animal.id}", style = MaterialTheme.typography.titleSmall)
                if (animal.lotId != 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.lot) + " ${animal.lotId}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = animalStateInfo.color.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(id = animalStateInfo.textRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = animalStateInfo.color,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}