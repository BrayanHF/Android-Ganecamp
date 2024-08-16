package com.ganecamp.ui.animals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.ui.general.DatePickerField
import com.ganecamp.ui.navigation.ScreenInternal
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State
import java.time.ZonedDateTime

@Composable
fun AnimalFormScreen(navController: NavController, animalId: Int = 0) {
    val viewModel: AnimalFormViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val lots by viewModel.lots.observeAsState(initial = emptyList())
    val animalSaved by viewModel.animalSaved.collectAsState()

    LaunchedEffect(animalId) {
        viewModel.loadLots()
        if (animalId != 0) {
            viewModel.loadAnimal(animalId)
        }
    }

    LaunchedEffect(animalSaved) {
        if (animalSaved) {
            navController.navigate("animalDetail/${state.id}/${state.lotId}") {
                popUpTo(ScreenInternal.AnimalForm.route) { inclusive = true }
                popUpTo(ScreenInternal.AnimalDetail.route) { inclusive = true }
            }
        }
    }

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

@Composable
fun AnimalFormTopBarContent() {
    Text(text = stringResource(id = R.string.add_animal))
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

@Composable
fun GenderDropdown(selectedGender: Gender, onGenderChange: (Gender) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Box {
        var icGender: Int
        var textGender: Int
        if (selectedGender == Gender.Male) {
            icGender = R.drawable.ic_bull
            textGender = R.string.male
        } else {
            icGender = R.drawable.ic_cow
            textGender = R.string.female
        }
        OutlinedTextField(value = stringResource(id = textGender),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = icGender),
                    contentDescription = stringResource(id = textGender),
                    modifier = Modifier.size(24.dp)
                )
            },
            onValueChange = {},
            label = { Text(stringResource(id = R.string.gender)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent { if (it.isFocused) expanded = true })
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, content = {
            Gender.entries.forEach { gender ->
                if (gender == Gender.Male) {
                    icGender = R.drawable.ic_bull
                    textGender = R.string.male
                } else {
                    icGender = R.drawable.ic_cow
                    textGender = R.string.female
                }
                DropdownMenuItem(leadingIcon = {
                    Icon(
                        painter = painterResource(id = icGender),
                        contentDescription = stringResource(id = textGender),
                        modifier = Modifier.size(24.dp)
                    )
                }, text = { Text(stringResource(id = textGender)) }, onClick = {
                    onGenderChange(gender)
                    expanded = false
                    focusManager.clearFocus()
                })
            }
        })

    }
}

@Composable
fun StateDropdown(selectedState: State, onStateChange: (State) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Box {
        var textState = when (selectedState) {
            State.Healthy -> R.string.healthy
            State.Sick -> R.string.sick
            State.Injured -> R.string.injured
            State.Dead -> R.string.dead
            State.Sold -> R.string.sold
        }
        OutlinedTextField(value = stringResource(id = textState),
            onValueChange = {},
            label = { Text(stringResource(id = R.string.state)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent { if (it.isFocused) expanded = true })
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            State.entries.forEach { state ->
                textState = when (state) {
                    State.Healthy -> R.string.healthy
                    State.Sick -> R.string.sick
                    State.Injured -> R.string.injured
                    State.Dead -> R.string.dead
                    State.Sold -> R.string.sold
                }
                DropdownMenuItem(text = { Text(stringResource(id = textState)) }, onClick = {
                    onStateChange(state)
                    expanded = false
                    focusManager.clearFocus()
                })
            }
        }
    }
}

@Composable
fun LotDropdown(lots: List<Int>, selectedLot: Int?, onLotChange: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val lotsWithZero = listOf(0) + lots

    Box {
        val valueField = selectedLot?.toString() ?: lotsWithZero.first().toString()
        OutlinedTextField(value = valueField,
            onValueChange = {},
            label = { Text(stringResource(id = R.string.lot)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent { if (it.isFocused) expanded = true })
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            lotsWithZero.forEach { lot ->
                DropdownMenuItem(text = { Text(lot.toString()) }, onClick = {
                    onLotChange(lot)
                    expanded = false
                    focusManager.clearFocus()
                })
            }
        }
    }
}