package com.ganecamp.ui.animals

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ganecamp.domain.model.Animal

@Composable
fun AnimalScreen(navController: NavHostController) {
    val viewModel: AnimalsViewModel = hiltViewModel()
    val animals by viewModel.animals.observeAsState(initial = emptyList())
    AnimalList(navController, animals)
}

@Composable
fun AnimalList(navController: NavHostController, animals: List<Animal>) {
    if (animals.isNullOrEmpty()) {
        Text(text = "esta vaina esta vacia")
    } else {
        LazyColumn {
            items(animals) { animal ->
                AnimalItem(navController, animal)
            }
        }
    }
}

@Composable
fun AnimalItem(navController: NavHostController, animal: Animal) {
    Text(text = "ID: ${animal.id}, Tag: ${animal.tag}, Gender: ${animal.gender}")
}

