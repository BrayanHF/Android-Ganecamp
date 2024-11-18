package com.ganecamp.ui.screen.weight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.ui.component.bar.GeneralTopBar
import com.ganecamp.ui.component.dialog.RepositoryErrorDialog
import com.ganecamp.ui.component.field.DatePickerField
import com.ganecamp.ui.component.field.NumberTextField
import com.ganecamp.domain.enums.RepositoryRespond

@Composable
fun WeightFormScreen(navController: NavController, animalId: String) {
    val viewModel: WeightFormViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val weightSaved by viewModel.weightSaved.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAnimalId(animalId)
    }

    LaunchedEffect(weightSaved) {
        if (weightSaved) {
            navController.popBackStack()
        }
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
        GeneralTopBar(title = stringResource(id = R.string.add_weight),
            onBackClick = { navController.popBackStack() })
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            WeightFormContent(viewModel = viewModel, state = state)
        }
    }
}

@Composable
fun WeightFormContent(viewModel: WeightFormViewModel, state: WeightFormState) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        NumberTextField(
            value = state.weight,
            onValueChange = { viewModel.onWeightChange(it) },
            label = stringResource(id = R.string.weight),
        )
        DatePickerField(
            selectedDate = state.date,
            onDateChange = { viewModel.onDateChange(it) },
            label = R.string.weight_date
        )
        Button(
            onClick = { viewModel.saveWeight() },
            modifier = Modifier.align(Alignment.End),
        ) {
            Text(stringResource(id = R.string.save))
        }
    }
}