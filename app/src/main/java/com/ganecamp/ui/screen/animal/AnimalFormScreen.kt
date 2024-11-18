package com.ganecamp.ui.screen.animal

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
import com.ganecamp.ui.component.bar.GeneralTopBar
import com.ganecamp.ui.component.dialog.RepositoryErrorDialog
import com.ganecamp.ui.component.field.DatePickerField
import com.ganecamp.ui.component.field.NumberTextField
import com.ganecamp.ui.navigation.screens.AnimalDetailNav
import com.ganecamp.ui.navigation.screens.AnimalFormNav
import com.ganecamp.ui.theme.Green
import com.ganecamp.ui.theme.Red
import com.ganecamp.ui.util.geAnimalGenderRes
import com.ganecamp.ui.util.getAnimalBreedRes
import com.ganecamp.ui.util.getAnimalStateRes
import com.ganecamp.domain.enums.AnimalBreed
import com.ganecamp.domain.enums.AnimalGender
import com.ganecamp.domain.enums.RepositoryRespond
import com.ganecamp.domain.enums.AnimalState

@Composable
fun AnimalFormScreen(navController: NavController, animalId: String?, tag: String) {
    val viewModel: AnimalFormViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val lots by viewModel.lots.collectAsState(initial = emptyList())
    val animalSaved by viewModel.animalSaved.collectAsState(initial = false)
    val error by viewModel.error.collectAsState(initial = RepositoryRespond.OK)

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
        if (error != RepositoryRespond.OK) {
            showError = true
        }
    }

    if (showError) {
        RepositoryErrorDialog(error = error, onDismiss = { showError = false })
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

        GenderDropdown(
            selectedAnimalGender = state.animalGender,
            onGenderChange = { viewModel.onGenderChange(it) })

        BreedDropdown(selectedAnimalBreed = state.animalBreed, onBreedChange = { viewModel.onBreedChange(it) })

        StateDropdown(selectedAnimalState = state.animalState, onStateChange = { viewModel.onStateChange(it) })

        DatePickerField(
            selectedDate = state.birthDate.toInstant(),
            onDateChange = { viewModel.onBirthDateChange(it) },
            label = R.string.birth_date
        )

        LotDropdown(lots = lots,
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

        if (state.animalState == AnimalState.Sold) {
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
fun GenderDropdown(selectedAnimalGender: AnimalGender, onGenderChange: (AnimalGender) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val (iconRes, textRes) = geAnimalGenderRes(selectedAnimalGender)

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(value = stringResource(id = textRes),
            trailingIcon = {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = stringResource(id = textRes),
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
            AnimalGender.entries.forEach { gender ->
                val (itemIconRes, itemTextRes) = geAnimalGenderRes(gender)
                DropdownMenuItem(onClick = {
                    onGenderChange(gender)
                    expanded = false
                }, text = {
                    Text(stringResource(id = itemTextRes))
                }, trailingIcon = {
                    Image(
                        painter = painterResource(id = itemIconRes),
                        contentDescription = stringResource(id = itemTextRes),
                        modifier = Modifier.size(24.dp)
                    )
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDropdown(selectedAnimalBreed: AnimalBreed, onBreedChange: (AnimalBreed) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val breedText = getAnimalBreedRes(selectedAnimalBreed)

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
            AnimalBreed.entries.forEach { breed ->
                val itemText = getAnimalBreedRes(breed)
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
fun StateDropdown(selectedAnimalState: AnimalState, onStateChange: (AnimalState) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val (textRes, colorRes) = getAnimalStateRes(selectedAnimalState)

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(value = stringResource(id = textRes),
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
                        .background(colorRes, shape = CircleShape)
                )
            })
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            AnimalState.entries.forEach { state ->
                val (itemTextRes, itemColorRes) = getAnimalStateRes(state)
                DropdownMenuItem(onClick = {
                    onStateChange(state)
                    expanded = false
                }, text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = itemTextRes), modifier = Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(itemColorRes, shape = CircleShape)
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