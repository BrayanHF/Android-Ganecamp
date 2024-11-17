package com.ganecamp.ui.lot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.data.firibase.model.Lot
import com.ganecamp.domain.services.LotService
import com.ganecamp.utilities.enums.FirestoreRespond
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class LotFormViewModel @Inject constructor(private val lotService: LotService) : ViewModel() {

    private val _uiState = MutableStateFlow(LotFormState())
    val uiState: StateFlow<LotFormState> = _uiState

    private val _lotSaved = MutableStateFlow(false)
    val lotSaved: StateFlow<Boolean> = _lotSaved

    private val _error = MutableStateFlow(FirestoreRespond.OK)
    val error: StateFlow<FirestoreRespond> = _error

    fun loadLot(lotId: String) {
        viewModelScope.launch {
            val lotRespond = lotService.getLotById(lotId)
            if (lotRespond.second == FirestoreRespond.OK) {
                lotRespond.first?.let { lot ->
                    _uiState.value = LotFormState(
                        id = lot.id,
                        purchaseValue = lot.purchaseValue.toString(),
                        purchaseDate = lot.purchaseDate.toInstant(),
                        saleValue = lot.saleValue.toString(),
                        saleDate = lot.saleDate.toInstant(),
                        sold = lot.sold
                    )
                }
            } else {
                _error.value = lotRespond.second
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onPurchasedAnimalsChange(purchasedAnimals: String) {
        _uiState.value = _uiState.value.copy(purchasedAnimals = purchasedAnimals)
    }

    fun onSoldChange(sold: Boolean) {
        _uiState.value = _uiState.value.copy(sold = sold)
    }

    fun onPurchaseValueChange(purchaseValue: String) {
        _uiState.value = _uiState.value.copy(purchaseValue = purchaseValue)
    }

    fun onPurchaseDateChange(purchaseDate: Instant) {
        _uiState.value = _uiState.value.copy(purchaseDate = purchaseDate)
    }

    fun onSaleValueChange(saleValue: String) {
        _uiState.value = _uiState.value.copy(saleValue = saleValue)
    }

    fun onSaleDateChange(saleDate: Instant) {
        _uiState.value = _uiState.value.copy(saleDate = saleDate)
    }

    private fun convertNumberSafely(value: String): Double {
        return try {
            value.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    fun saveLot() {
        viewModelScope.launch {
            val currentState = _uiState.value

            val lot = Lot(
                id = currentState.id,
                name = currentState.name,
                purchasedAnimals = convertNumberSafely(currentState.purchasedAnimals).toInt(),
                purchaseValue = convertNumberSafely(currentState.purchaseValue),
                purchaseDate = Timestamp(currentState.purchaseDate),
                saleValue = convertNumberSafely(currentState.saleValue),
                saleDate = Timestamp(currentState.saleDate),
                sold = currentState.sold
            )

            if (lot.id == null) {
                val (newLotId, respond) = lotService.createLot(lot)
                if (respond == FirestoreRespond.OK) {
                    _lotSaved.value = true
                    _uiState.value = _uiState.value.copy(id = newLotId)
                } else {
                    _error.value = respond
                }
            } else {
                val updateRespond = lotService.updateLot(lot)
                if (updateRespond == FirestoreRespond.OK) {
                    _lotSaved.value = true
                } else {
                    _error.value = updateRespond
                }
            }
        }
    }

}

data class LotFormState(
    val id: String? = null,
    val name: String = "",
    val purchasedAnimals: String = "",
    val purchaseValue: String = "",
    val purchaseDate: Instant = Instant.now(),
    val saleValue: String = "",
    val saleDate: Instant = Instant.now(),
    val sold: Boolean = false
)