package com.ganecamp.ui.animal

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
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.data.firibase.model.Lot
import com.ganecamp.ui.general.DatePickerField
import com.ganecamp.ui.general.GeneralTopBar
import com.ganecamp.ui.general.NumberTextField
import com.ganecamp.ui.general.ShowFirestoreError
import com.ganecamp.ui.general.geAnimalGenderInfo
import com.ganecamp.ui.general.getAnimalStateInfo
import com.ganecamp.ui.general.getBreedText
import com.ganecamp.ui.navigation.AnimalDetailNav
import com.ganecamp.ui.navigation.AnimalFormNav
import com.ganecamp.ui.theme.Green
import com.ganecamp.ui.theme.Red
import com.ganecamp.utilities.enums.Breed
import com.ganecamp.utilities.enums.FirestoreRespond
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State

@Composable
fun AnimalFormScreen(navController: NavController, animalId: String?, tag: String) {
    val viewModel: AnimalFormViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val lots by viewModel.lots.collectAsState(initial = emptyList())
    val animalSaved by viewModel.animalSaved.collectAsState(initial = false)
    val error by viewModel.error.collectAsState(initial = FirestoreRespond.OK)

    LaunchedEffect(animalId) {
        viewModel.loadLots()
        viewModel.loadTag(tag)
        if (animalId != null) {
            viewModel.loadAnimal(animalId)
        }
    }

    LaunchedEffect(animalSaved) {
        if (animalSaved) {
            navController.navigate(AnimalDetailNav(state.id!!)) {
                popUpTo(AnimalFormNav(state.id, state.tag)) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    var showError by remember { mutableStateOf(false) }
    LaunchedEffect(error) {
        if (error != FirestoreRespond.OK) {
            showError = true
        }
    }

    if (showError) {
        ShowFirestoreError(error = error, onDismiss = { showError = false })
    }

    Scaffold(topBar = {
        GeneralTopBar(title = if (animalId == null) stringResource(id = R.string.new_animal)
        else stringResource(id = R.string.edit_animal),
            onBackClick = { navController.popBackStack() })
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimalFormContent(
                state = state, lots = lots, viewModel = viewModel
            )
        }
    }
}

@Composable
fun AnimalFormContent(
    state: AnimalFormState, lots: List<Lot>, viewModel: AnimalFormViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        OutlinedTextField(value = state.nickname ?: "",
            onValueChange = { viewModel.onNicknameChange(it) },
            label = { Text(stringResource(id = R.string.nickname) + " - " + stringResource(id = R.string.optional)) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_nickname),
                    contentDescription = stringResource(id = R.string.nickname),
                    modifier = Modifier.size(24.dp)
                )
            })

        GenderDropdown(selectedGender = state.gender,
            onGenderChange = { viewModel.onGenderChange(it) })

        BreedDropdown(selectedBreed = state.breed, onBreedChange = { viewModel.onBreedChange(it) })

        StateDropdown(selectedState = state.state, onStateChange = { viewModel.onStateChange(it) })

        DatePickerField(
            selectedDate = state.birthDate.toInstant(),
            onDateChange = { viewModel.onBirthDateChange(it) },
            label = R.string.birth_date
        )

        LotDropdown(
            lots = lots,
            selectedLot = state.lotId,
            onLotChange = { viewModel.onLotChange(it) })

        if (state.id == null) {
            NumberTextField(value = state.weight, onValueChange = {
                viewModel.onWeightChange(it)
            }, label = stringResource(id = R.string.weight), trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_weight),
                    contentDescription = stringResource(id = R.string.weight),
                    modifier = Modifier.size(24.dp)
                )
            })
        }

        DatePickerField(
            selectedDate = state.purchaseDate.toInstant(),
            onDateChange = { viewModel.onPurchaseDateChange(it) },
            label = R.string.purchase_date,
            icon = R.drawable.ic_purchase_date
        )

        NumberTextField(value = state.purchaseValue, onValueChange = {
            viewModel.onPurchaseValueChange(it)
        }, label = stringResource(id = R.string.purchase_value), trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_dollar),
                contentDescription = stringResource(id = R.string.purchase_value),
                modifier = Modifier.size(24.dp),
                tint = Red
            )
        })

        if (state.state == State.Sold) {
            DatePickerField(
                selectedDate = state.saleDate.toInstant(),
                onDateChange = { viewModel.onSaleDateChange(it) },
                label = R.string.sale_date,
                icon = R.drawable.ic_sale_date
            )

            NumberTextField(value = state.saleValue, onValueChange = {
                viewModel.onSaleValueChange(it)
            }, label = stringResource(id = R.string.sale_value), trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dollar),
                    contentDescription = stringResource(id = R.string.sale_value),
                    modifier = Modifier.size(24.dp),
                    tint = Green
                )
            })
        }

        Button(
            onClick = { viewModel.saveAnimal() },
            modifier = Modifier.align(Alignment.End),
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
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
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
fun BreedDropdown(selectedBreed: Breed, onBreedChange: (Breed) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val breedText = getBreedText(selectedBreed)

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(value = stringResource(id = breedText),
            onValueChange = {},
            label = { Text(stringResource(id = R.string.breed)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_breed),
                    contentDescription = stringResource(id = R.string.breed),
                    modifier = Modifier.size(24.dp)
                )
            })
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Breed.entries.forEach { breed ->
                val itemText = getBreedText(breed)
                DropdownMenuItem(onClick = {
                    onBreedChange(breed)
                    expanded = false
                }, text = {
                    Text(stringResource(id = itemText))
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
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
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
fun LotDropdown(lots: List<Lot>, selectedLot: String?, onLotChange: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val lotsWithNull = listOf(null) + lots

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(value = formatLot(selectedLot),
            onValueChange = {},
            label = { Text(stringResource(id = R.string.lot)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
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
            lotsWithNull.forEach { lot ->
                DropdownMenuItem(onClick = {
                    if (lot != null) {
                        onLotChange(lot.id)
                    } else {
                        onLotChange(null)
                    }
                    expanded = false
                }, text = {
                    if (lot != null) {
                        Text(formatLot(lot.name))
                    } else {
                        Text("ND")
                    }
                })
            }
        }
    }
}

private fun formatLot(lotName: String?) =
    if (lotName == null || lotName == "") "ND" else lotName.toString()