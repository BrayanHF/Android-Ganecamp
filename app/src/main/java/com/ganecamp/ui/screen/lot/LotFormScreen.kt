package com.ganecamp.ui.screen.lot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.domain.enums.RepositoryRespond
import com.ganecamp.ui.component.bar.GenericTopBar
import com.ganecamp.ui.component.button.ToggleButtons
import com.ganecamp.ui.component.dialog.RepositoryErrorDialog
import com.ganecamp.ui.component.field.DatePickerField
import com.ganecamp.ui.component.field.NumberTextField
import com.ganecamp.ui.component.misc.IsLoading
import com.ganecamp.ui.navigation.screens.LotDetailNav
import com.ganecamp.ui.navigation.screens.LotFormNav

//Todo: Change how the screen show the errors and add icons for the text fields
@Composable
fun LotFormScreen(navController: NavController, lotId: String?) {
    val viewModel: LotFormViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val lotSaved by viewModel.lotSaved.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(lotId) {
        if (lotId != null) {
            viewModel.loadLot(lotId)
        } else {
            viewModel.nothingToLoad()
        }
    }

    LaunchedEffect(lotSaved) {
        if (lotSaved) {
            state.id?.let { newLotId ->
                if (lotId == null) {
                    navController.navigate(LotDetailNav(newLotId)) {
                        popUpTo(LotFormNav(null)) { inclusive = true }
                    }
                } else {
                    navController.popBackStack()
                }
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

    if (isLoading) {
        IsLoading()
    } else {
        Scaffold(topBar = {
            GenericTopBar(
                onBackClick = { navController.popBackStack() },
                title = if (lotId == null) stringResource(id = R.string.add_lot)
                else stringResource(id = R.string.edit_lot),
            )
        }) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AnimalFormContent(
                    state = state,
                    viewModel = viewModel,
                )
            }
        }
    }
}

@Composable
fun AnimalFormContent(
    state: LotFormState, viewModel: LotFormViewModel
) {
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = state.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text(stringResource(id = R.string.name)) },
            modifier = Modifier.fillMaxWidth()
        )

        NumberTextField(
            value = state.purchasedAnimals,
            onValueChange = { viewModel.onPurchasedAnimalsChange(it) },
            label = stringResource(id = R.string.purchased_animals)
        )

        NumberTextField(
            value = state.purchaseValue,
            onValueChange = { viewModel.onPurchaseValueChange(it) },
            label = stringResource(id = R.string.purchase_value)
        )

        DatePickerField(label = R.string.purchase_date,
            selectedDate = state.purchaseDate,
            onDateChange = { viewModel.onPurchaseDateChange(it) })

        ToggleButtons(
            txtLeftButton = stringResource(id = R.string.sold),
            txtRightButton = stringResource(R.string.not_sold),
            onClickLeft = { viewModel.onSoldChange(it) },
            onCLickRight = { viewModel.onSoldChange(!it) },
            leftButtonSelectedFirsts = state.sold
        )

        if (state.sold) {
            NumberTextField(
                value = state.saleValue,
                onValueChange = { viewModel.onSaleValueChange(it) },
                label = stringResource(id = R.string.sale_value)
            )
            DatePickerField(label = R.string.sale_date,
                selectedDate = state.saleDate,
                onDateChange = { viewModel.onSaleDateChange(it) })
        }

        Button(
            onClick = { viewModel.saveLot() }, modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(id = R.string.save))
        }
    }
}