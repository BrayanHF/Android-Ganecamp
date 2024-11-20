package com.ganecamp.ui.screen.event

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
import com.ganecamp.data.firibase.model.Event
import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.enums.RepositoryRespond
import com.ganecamp.ui.component.bar.GenericTopBar
import com.ganecamp.ui.component.button.ToggleButtons
import com.ganecamp.ui.component.field.DatePickerField

@Composable
fun EventAddFormScreen(navController: NavHostController, entityId: String, entityType: EntityType) {
    val viewModel: EventAddFormViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState(initial = emptyList())
    val eventSaved by viewModel.eventSaved.collectAsState(initial = false)
    val error by viewModel.error.collectAsState(initial = RepositoryRespond.OK)

    LaunchedEffect(Unit) {
        viewModel.loadEntityType(entityId, entityType)
        viewModel.loadEvents()
    }

    LaunchedEffect(eventSaved) {
        if (eventSaved) {
            navController.popBackStack()
        }
    }

    var showError by remember { mutableStateOf(false) }
    LaunchedEffect(error) {
        if (error != RepositoryRespond.OK) {
            showError = true
        }
    }

    Scaffold(topBar = {
        GenericTopBar(title = stringResource(id = R.string.add_event),
            onBackClick = { navController.popBackStack() })
    }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            EventFormContent(viewModel, state, events)
        }
    }


}

@Composable
fun EventFormContent(
    viewModel: EventAddFormViewModel, state: EventFormState, vaccines: List<Event>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (vaccines.isNotEmpty()) {
            ToggleButtons(txtLeftButton = stringResource(id = R.string.new_event),
                txtRightButton = stringResource(id = R.string.old_event),
                onClickLeft = { viewModel.onIsNewChange(it) },
                onCLickRight = { viewModel.onIsNewChange(!it) })
        }
        if (state.isNew) {
            OutlinedTextField(value = state.title,
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text(stringResource(id = R.string.title)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(value = state.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text(stringResource(id = R.string.event_description)) },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            EventDropdown(events = vaccines,
                onEventChange = { id, title -> viewModel.onEventSelected(id, title) })
        }

        DatePickerField(
            selectedDate = state.date,
            onDateChange = { viewModel.onDateChange(it) },
            label = R.string.application_date
        )

        Button(
            onClick = { viewModel.saveEvent() },
            modifier = Modifier.align(Alignment.End),
            enabled = if (state.isNew) state.title.isNotEmpty() && state.description.isNotEmpty()
            else state.entityId.isNotEmpty()
        ) {
            Text(text = stringResource(id = R.string.save))
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDropdown(
    events: List<Event>,
    onEventChange: (String, String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var nameForSearch by remember { mutableStateOf("") }

    val filteredVaccines = remember(nameForSearch) {
        if (nameForSearch.isEmpty()) events else events.filter {
            it.title.contains(nameForSearch, ignoreCase = true)
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
            label = { Text(stringResource(id = R.string.look_for_event)) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_event),
                    contentDescription = stringResource(id = R.string.look_for_event),
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
                    onEventChange(vaccine.id!!, vaccine.title)
                    nameForSearch = vaccine.title
                    expanded = false
                }, text = { Text(vaccine.title) })
            }
        }
    }
}