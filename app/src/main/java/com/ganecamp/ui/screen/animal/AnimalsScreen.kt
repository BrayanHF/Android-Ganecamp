package com.ganecamp.ui.screen.animal

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ganecamp.R
import com.ganecamp.domain.model.Animal
import com.ganecamp.ui.component.card.AnimalCard
import com.ganecamp.ui.component.dialog.ErrorDialog
import com.ganecamp.ui.component.misc.IsLoading
import com.ganecamp.ui.component.misc.NoRegistered
import com.ganecamp.ui.navigation.screens.AnimalDetailNav

@Composable
fun AnimalScreen(navController: NavHostController) {
    val viewModel: AnimalsViewModel = hiltViewModel()
    val animals by viewModel.animals.collectAsState(emptyList())
    val isLoading by viewModel.isLoading.collectAsState(true)
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAnimals()
    }

    if (error != null) {
        ErrorDialog(error = error!!, onDismiss = { viewModel.dismissError() })
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
            AnimalCard(onClick = {
                animal.id?.let { id ->
                    navController.navigate(AnimalDetailNav(id))
                }
            }, animal = animal, showLot = true)
        }
    }

}