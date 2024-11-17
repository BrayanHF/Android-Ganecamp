package com.ganecamp.ui.animal.vaccine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ganecamp.R
import com.ganecamp.data.firibase.model.Vaccine
import com.ganecamp.ui.general.DatePickerField
import com.ganecamp.ui.general.GeneralTopBar
import com.ganecamp.ui.general.ToggleButtons
import com.ganecamp.utilities.enums.FirestoreRespond

@Composable
fun VaccineAddFormScreen(navController: NavHostController, animalId: String) {
    val viewModel: VaccineAddFormViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val vaccines by viewModel.vaccines.collectAsState(initial = emptyList())
    val vaccineSaved by viewModel.vaccineSaved.collectAsState(initial = false)
    val error by viewModel.error.collectAsState(initial = FirestoreRespond.OK)

    LaunchedEffect(Unit) {
        viewModel.loadAnimalId(animalId)
        viewModel.loadVaccines()
    }

    LaunchedEffect(vaccineSaved) {
        if (vaccineSaved) {
            navController.popBackStack()
        }
    }

    var showError by remember { mutableStateOf(false) }
    LaunchedEffect(error) {
        if (error != FirestoreRespond.OK) {
            showError = true
        }
    }

    Scaffold(topBar = {
        GeneralTopBar(title = stringResource(id = R.string.add_vaccine),
            onBackClick = { navController.popBackStack() })
    }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            VaccineFormContent(viewModel, state, vaccines)
        }
    }


}

@Composable
fun VaccineFormContent(
    viewModel: VaccineAddFormViewModel, state: VaccineFormState, vaccines: List<Vaccine>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (vaccines.isNotEmpty()) {
            ToggleButtons(txtFirstButton = stringResource(id = R.string.new_vaccine),
                txtSecondButton = stringResource(id = R.string.old_vaccine),
                onSelectionChange = { viewModel.onIsNewChange(it) })
        }
        if (state.isNew) {
            OutlinedTextField(value = state.name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text(stringResource(id = R.string.vaccine_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(value = state.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text(stringResource(id = R.string.vaccine_description)) },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            VaccineDropdown(vaccines = vaccines,
                onVaccineChange = { id, name -> viewModel.onVaccineSelected(id, name) })
        }

        DatePickerField(
            selectedDate = state.date,
            onDateChange = { viewModel.onDateChange(it) },
            label = R.string.application_date
        )

        Button(
            onClick = { viewModel.saveVaccine() },
            modifier = Modifier.align(Alignment.End),
            enabled = if (state.isNew) state.name.isNotEmpty() && state.description.isNotEmpty()
            else state.vaccineId.isNotEmpty()
        ) {
            Text(text = stringResource(id = R.string.save))
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccineDropdown(
    vaccines: List<Vaccine>,
    onVaccineChange: (String, String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var nameForSearch by remember { mutableStateOf("") }

    val filteredVaccines = remember(nameForSearch) {
        if (nameForSearch.isEmpty()) vaccines else vaccines.filter {
            it.name.contains(nameForSearch, ignoreCase = true)
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(value = nameForSearch,
            onValueChange = {
                nameForSearch = it
                expanded = true
            },
            label = { Text(stringResource(id = R.string.look_for_vaccine)) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_vaccine),
                    contentDescription = stringResource(id = R.string.look_for_vaccine),
                    modifier = Modifier.size(24.dp)
                )
            })

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            filteredVaccines.forEach { vaccine ->
                DropdownMenuItem(onClick = {
                    onVaccineChange(vaccine.id!!, vaccine.name)
                    nameForSearch = vaccine.name
                    expanded = false
                }, text = { Text(vaccine.name) })
            }
        }
    }
}