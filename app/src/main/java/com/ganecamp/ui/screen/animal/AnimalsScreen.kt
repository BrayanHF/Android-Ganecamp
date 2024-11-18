package com.ganecamp.ui.screen.animal

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.ganecamp.data.firibase.model.Animal
import com.ganecamp.ui.component.dialog.RepositoryErrorDialog
import com.ganecamp.ui.component.misc.IsLoading
import com.ganecamp.ui.component.misc.NoRegistered

import com.ganecamp.ui.navigation.screens.AnimalDetailNav
import com.ganecamp.ui.util.getAnimalBreedRes
import com.ganecamp.ui.util.getAnimalStateRes
import com.ganecamp.domain.enums.AnimalGender
import com.ganecamp.domain.enums.RepositoryRespond

@Composable
fun AnimalScreen(navController: NavHostController) {
    val viewModel: AnimalsViewModel = hiltViewModel()
    val animals by viewModel.animals.collectAsState(emptyList())
    val isLoading by viewModel.isLoading.collectAsState(true)
    val error by viewModel.error.collectAsState(RepositoryRespond.OK)

    LaunchedEffect(Unit) {
        viewModel.loadAnimals()
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
    val animalGenderIcon: Int = if (animal.animalGender == AnimalGender.Male) {
        R.drawable.ic_bull
    } else {
        R.drawable.ic_cow
    }
    val (textRes, colorRes) = getAnimalStateRes(animal.animalState)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { navController.navigate(AnimalDetailNav(animal.id!!)) },
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                animal.nickname?.let { nickname ->
                    Text(
                        text = nickname,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp)
                    )
                }
                Text(
                    text = stringResource(id = R.string.tag) + ": ${animal.tag}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp)
                )
            }
            if (animal.lotId != null) {
                Text(
                    text = stringResource(id = R.string.lot) + ": ${animal.lotId}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = animalGenderIcon),
                contentDescription = stringResource(R.string.animal_icon),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = getAnimalBreedRes(animal.animalBreed)),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = colorRes.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(id = textRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorRes,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}