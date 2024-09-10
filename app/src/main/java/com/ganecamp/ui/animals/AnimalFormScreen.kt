package com.ganecamp.ui.animals

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.ui.general.DatePickerField
import com.ganecamp.ui.general.ErrorMessages
import com.ganecamp.ui.general.NumberTextField
import com.ganecamp.ui.general.TopBar
import com.ganecamp.ui.general.geAnimalGenderInfo
import com.ganecamp.ui.general.getAnimalStateInfo
import com.ganecamp.ui.general.validateNumber
import com.ganecamp.ui.navigation.AnimalDetailNav
import com.ganecamp.ui.navigation.AnimalFormNav
import com.ganecamp.ui.theme.Green
import com.ganecamp.ui.theme.Red
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
    val errorMessages = ErrorMessages(
        moreThanOnePointError = stringResource(id = R.string.error_more_than_one_point),
        invalidCharactersError = stringResource(id = R.string.error_invalid_characters)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
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

        val weightError = validateNumber(state.weight, errorMessages)
        if (state.id == 0) {
            NumberTextField(value = state.weight,
                onValueChange = {
                    onWeightChange(it)
                },
                label = stringResource(id = R.string.weight),
                isError = weightError != null,
                errorMessage = weightError ?: "",
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_weight),
                        contentDescription = stringResource(id = R.string.weight),
                        modifier = Modifier.size(24.dp)
                    )
                })
        }

        val purchaseValueError = validateNumber(state.purchaseValue, errorMessages)
        NumberTextField(value = state.purchaseValue,
            onValueChange = {
                onPurchaseValueChange(it)
            },
            label = stringResource(id = R.string.purchase_value),
            isError = purchaseValueError != null,
            errorMessage = purchaseValueError ?: "",
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dollar),
                    contentDescription = stringResource(id = R.string.purchase_value),
                    modifier = Modifier.size(24.dp),
                    tint = Red
                )
            })

        val saleValueError = validateNumber(state.saleValue, errorMessages)
        if (state.state == State.Sold) {
            NumberTextField(value = state.saleValue,
                onValueChange = {
                    onSaleValueChange(it)
                },
                label = stringResource(id = R.string.sale_value),
                isError = saleValueError != null,
                errorMessage = saleValueError ?: "",
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_dollar),
                        contentDescription = stringResource(id = R.string.sale_value),
                        modifier = Modifier.size(24.dp),
                        tint = Green
                    )
                })
        }

        Button(
            onClick = onSaveClick,
            modifier = Modifier.align(Alignment.End),
            enabled = (state.id != 0 || weightError == null) && purchaseValueError == null && (state.state != State.Sold || saleValueError == null)
        ) {
            Text(stringResource(id = R.string.save))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown(selectedGender: Gender, onGenderChange: (Gender) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val genderInfo = geAnimalGenderInfo(selectedGender)

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(value = stringResource(id = genderInfo.textRes),
            trailingIcon = {
                Image(
                    painter = painterResource(id = genderInfo.iconRes),
                    contentDescription = stringResource(id = genderInfo.textRes),
                    modifier = Modifier.size(24.dp)
                )
            },
            onValueChange = {},
            label = { Text(stringResource(id = R.string.gender)) },
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
                val itemInfo = geAnimalGenderInfo(gender)
                DropdownMenuItem(onClick = {
                    onGenderChange(gender)
                    expanded = false
                }, text = {
                    Text(stringResource(id = itemInfo.textRes))
                }, trailingIcon = {
                    Image(
                        painter = painterResource(id = itemInfo.iconRes),
                        contentDescription = stringResource(id = itemInfo.textRes),
                        modifier = Modifier.size(24.dp)
                    )
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateDropdown(selectedState: State, onStateChange: (State) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val stateInfo = getAnimalStateInfo(selectedState)

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(value = stringResource(id = stateInfo.textRes),
            onValueChange = {},
            label = { Text(stringResource(id = R.string.state)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(stateInfo.color, shape = CircleShape)
                )
            })
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            State.entries.forEach { state ->
                val itemInfo = getAnimalStateInfo(state)
                DropdownMenuItem(onClick = {
                    onStateChange(state)
                    expanded = false
                }, text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = itemInfo.textRes),
                            modifier = Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(itemInfo.color, shape = CircleShape)
                        )
                    }
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

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(value = formatLot(selectedLot),
            onValueChange = {},
            label = { Text(stringResource(id = R.string.lot)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lot),
                    contentDescription = stringResource(id = R.string.lots),
                    modifier = Modifier.size(24.dp)
                )
            })
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            lotsWithZero.forEach { lot ->
                DropdownMenuItem(onClick = {
                    onLotChange(lot)
                    expanded = false
                }, text = {
                    Text(formatLot(lot))
                })
            }
        }
    }
}

private fun formatLot(lot: Int?) = if (lot == null || lot == 0) "ND" else lot.toString()