package com.ganecamp.ui.lot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.ui.general.DatePickerField
import com.ganecamp.ui.navigation.ScreenInternal
import java.time.ZonedDateTime

@Composable
fun LotFormScreen(navController: NavController, lotId: Int = 0) {
    val viewModel: LotFormViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val lotSaved by viewModel.lotSaved.collectAsState()


    LaunchedEffect(lotId) {
        if (lotId != 0) {
            viewModel.loadLot(lotId)
        }
    }

    LaunchedEffect(lotSaved) {
        if (lotSaved) {
            navController.navigate("lot") {
                popUpTo(ScreenInternal.LotForm.route) { inclusive = true }
            }
        }
    }

    AnimalFormContent(state = state,
        onPurchaseValueChange = { viewModel.onPurchaseValueChange(it) },
        onPurchaseDateChange = { viewModel.onPurchaseDateChange(it) },
        onSaleValueChange = { viewModel.onSaleValueChange(it) },
        onSaleDateChange = { viewModel.onSaleDateChange(it) },
        onSaveClick = {
            viewModel.saveLot()
        })
}

@Composable
fun LotFormTopBarContent() {
    Text(text = stringResource(id = R.string.add_animal))
}

@Composable
fun AnimalFormContent(
    state: LotFormState,
    onPurchaseValueChange: (String) -> Unit,
    onPurchaseDateChange: (ZonedDateTime) -> Unit,
    onSaleValueChange: (String) -> Unit,
    onSaleDateChange: (ZonedDateTime) -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        OutlinedTextField(
            value = state.purchaseValue,
            onValueChange = onPurchaseValueChange,
            label = { Text(text = stringResource(id = R.string.purchase_value)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        DatePickerField(
            selectedDate = state.purchaseDate,
            onDateChange = onPurchaseDateChange,
            label = R.string.purchase_date
        )

        OutlinedTextField(
            value = state.saleValue,
            onValueChange = onSaleValueChange,
            label = { Text(stringResource(id = R.string.sale_value)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        DatePickerField(
            selectedDate = state.saleDate,
            onDateChange = onSaleDateChange,
            label = R.string.sale_date
        )

        Button(
            onClick = onSaveClick, modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(id = R.string.save))
        }
    }
}