package com.ganecamp.ui.animals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.ui.general.DatePickerField
import com.ganecamp.ui.general.TopBar
import com.ganecamp.ui.navigation.AnimalDetailNav
import com.ganecamp.ui.navigation.AnimalFormNav
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State
import java.time.ZonedDateTime

@Composable
fun AnimalFormScreen(navController: NavController, animalId: Int = 0, tag: String) {
    val viewModel: AnimalFormViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val lots by viewModel.lots.observeAsState(initial = emptyList())
    val animalSaved by viewModel.animalSaved.collectAsState()

    LaunchedEffect(animalId) {
        viewModel.loadLots()
        viewModel.loadTag(tag)
        if (animalId != 0) {
            viewModel.loadAnimal(animalId)
        }
    }

    LaunchedEffect(animalSaved) {
        if (animalSaved) {
            navController.navigate(AnimalDetailNav(state.id)) {
                popUpTo(AnimalFormNav(state.id, state.tag)) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Scaffold(topBar = {
        TopBar(title = if (animalId == 0) stringResource(id = R.string.new_animal)
        else stringResource(id = R.string.edit_animal),
            onBackClick = { navController.popBackStack() })
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimalFormContent(state = state,
                lots = lots,
                onGenderChange = { viewModel.onGenderChange(it) },
                onBirthDateChange = { viewModel.onBirthDateChange(it) },
                onPurchaseValueChange = { viewModel.onPurchaseValueChange(it) },
                onSaleValueChange = { viewModel.onSaleValueChange(it) },
                onStateChange = { viewModel.onStateChange(it) },
                onWeightChange = { viewModel.onWeightChange(it) },
                onLotChange = { viewModel.onLotChange(it) },
                onSaveClick = {
                    viewModel.saveAnimal()
                })
        }
    }
}

@Composable
fun AnimalFormContent(
    state: AnimalFormState,
    lots: List<Int>,
    onGenderChange: (Gender) -> Unit,
    onBirthDateChange: (ZonedDateTime) -> Unit,
    onPurchaseValueChange: (String) -> Unit,
    onSaleValueChange: (String) -> Unit,
    onStateChange: (State) -> Unit,
    onWeightChange: (String) -> Unit,
    onLotChange: (Int) -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        GenderDropdown(selectedGender = state.gender, onGenderChange = onGenderChange)

        StateDropdown(selectedState = state.state, onStateChange = onStateChange)

        DatePickerField(
            selectedDate = state.birthDate,
            onDateChange = onBirthDateChange,
            label = R.string.birth_date
        )

        LotDropdown(lots = lots, selectedLot = state.lotId, onLotChange = onLotChange)

        if (state.id == 0) {
            OutlinedTextField(
                value = state.weight,
                onValueChange = onWeightChange,
                label = { Text(text = stringResource(id = R.string.weight)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        OutlinedTextField(
            value = state.purchaseValue,
            onValueChange = onPurchaseValueChange,
            label = { Text(stringResource(id = R.string.purchase_value)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        if (state.state == State.Sold) {
            OutlinedTextField(
                value = state.saleValue,
                onValueChange = onSaleValueChange,
                label = { Text(stringResource(id = R.string.sale_value)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = onSaveClick, modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(id = R.string.save))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown(selectedGender: Gender, onGenderChange: (Gender) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    val (icGender, textGender) = when (selectedGender) {
        Gender.Male -> R.drawable.ic_bull to R.string.male
        else -> R.drawable.ic_cow to R.string.female
    }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(value = stringResource(id = textGender),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = icGender),
                    contentDescription = stringResource(id = textGender),
                    modifier = Modifier.size(24.dp)
                )
            },
            onValueChange = {},
            label = { Text(text = stringResource(id = R.string.gender)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Gender.entries.forEach { gender ->
                val (itemIcGender, itemTextGender) = when (gender) {
                    Gender.Male -> R.drawable.ic_bull to R.string.male
                    else -> R.drawable.ic_cow to R.string.female
                }
                DropdownMenuItem(leadingIcon = {
                    Icon(
                        painter = painterResource(id = itemIcGender),
                        contentDescription = stringResource(id = itemTextGender),
                        modifier = Modifier.size(24.dp)
                    )
                }, onClick = {
                    onGenderChange(gender)
                    expanded = false
                }, text = {
                    Text(stringResource(id = itemTextGender))
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateDropdown(selectedState: State, onStateChange: (State) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    // Define your text based on the selected state
    val textState = when (selectedState) {
        State.Healthy -> R.string.healthy
        State.Sick -> R.string.sick
        State.Injured -> R.string.injured
        State.Dead -> R.string.dead
        State.Sold -> R.string.sold
    }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(value = stringResource(id = textState),
            onValueChange = {},
            label = { Text(stringResource(id = R.string.state)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            State.entries.forEach { state ->
                val itemTextState = when (state) {
                    State.Healthy -> R.string.healthy
                    State.Sick -> R.string.sick
                    State.Injured -> R.string.injured
                    State.Dead -> R.string.dead
                    State.Sold -> R.string.sold
                }
                DropdownMenuItem(text = { Text(stringResource(id = itemTextState)) }, onClick = {
                    onStateChange(state)
                    expanded = false
                })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotDropdown(lots: List<Int>, selectedLot: Int?, onLotChange: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val lotsWithZero = listOf(0) + lots

    val valueField = selectedLot?.toString() ?: lotsWithZero.first().toString()

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(value = valueField,
            onValueChange = {},
            label = { Text(stringResource(id = R.string.lot)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = {
            expanded = false
        }, modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            lotsWithZero.forEach { lot ->
                DropdownMenuItem(text = { Text(lot.toString()) }, onClick = {
                    onLotChange(lot)
                    expanded = false
                })
            }
        }
    }
}