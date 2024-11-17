package com.ganecamp.ui.lot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import com.ganecamp.ui.general.DatePickerField
import com.ganecamp.ui.general.NumberTextField
import com.ganecamp.ui.general.ShowFirestoreError
import com.ganecamp.ui.general.ToggleButtons
import com.ganecamp.ui.navigation.LotDetailNav
import com.ganecamp.ui.navigation.LotFormNav
import com.ganecamp.utilities.enums.FirestoreRespond

//Todo: Change how the screen show the errors and add icons for the text fields
@Composable
fun LotFormScreen(navController: NavController, lotId: String?) {
    val viewModel: LotFormViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val lotSaved by viewModel.lotSaved.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(lotId) {
        if (lotId != null) {
            viewModel.loadLot(lotId)
        }
    }

    LaunchedEffect(lotSaved) {
        if (lotSaved) {
            state.id?.let { newLotId ->
                navController.navigate(LotDetailNav(newLotId)) {
                    popUpTo(LotFormNav(null)) { inclusive = true }
                }
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

    AnimalFormContent(
        state = state,
        viewModel = viewModel,
    )
}

@Composable
fun AnimalFormContent(
    state: LotFormState, viewModel: LotFormViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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

        ToggleButtons(txtFirstButton = stringResource(id = R.string.sold),
            txtSecondButton = stringResource(R.string.not_sold),
            onSelectionChange = { viewModel.onSoldChange(it) })

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