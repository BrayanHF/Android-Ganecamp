package com.ganecamp.ui.screen.lot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.data.firibase.model.Animal
import com.ganecamp.data.firibase.model.EventApplied
import com.ganecamp.data.firibase.model.Lot
import com.ganecamp.ui.component.dialog.RepositoryErrorDialog
import com.ganecamp.domain.enums.RepositoryRespond

//Todo: Change all the design and do the logic, this is for all the screen
@Composable
fun LotDetailScreen(navController: NavController, lotId: String) {
    val viewModel: LotDetailViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading.collectAsState(initial = true)
    val lot by viewModel.lot.collectAsState()
    val events: List<EventApplied> by viewModel.events.collectAsState()
    val animals: List<Animal> by viewModel.animals.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(lotId) {
        viewModel.loadLot(lotId)
        viewModel.loadEvents(lotId)
        viewModel.loadAnimals(lotId)
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

    if (!isLoading) {
        lot?.let { lotDetail -> LotInfo(lotDetail = lotDetail) } ?: navController.popBackStack()
    }

}

@Composable
fun LotInfo(lotDetail: Lot) { //Todo: Lot info
}