package com.ganecamp.ui.lot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.services.LotService
import com.ganecamp.model.objects.Lot
import com.ganecamp.utilities.enums.FirestoreRespond
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
                        purchaseDate = lot.purchaseDate,
                        saleValue = lot.saleValue.toString(),
                        saleDate = lot.saleDate,
                        sold = lot.sold
                    )
                }
            } else {
                _error.value = lotRespond.second
            }
        }
    }

    fun onPurchaseValueChange(purchaseValue: String) {
        _uiState.value = _uiState.value.copy(purchaseValue = purchaseValue)
    }

    fun onPurchaseDateChange(purchaseDate: Timestamp) {
        _uiState.value = _uiState.value.copy(purchaseDate = purchaseDate)
    }

    fun onSaleValueChange(saleValue: String) {
        _uiState.value = _uiState.value.copy(saleValue = saleValue)
    }

    fun onSaleDateChange(saleDate: Timestamp) {
        _uiState.value = _uiState.value.copy(saleDate = saleDate)
    }

    fun saveLot() {
        viewModelScope.launch {
            val currentState = _uiState.value

            val purchaseValue: Double = try {
                currentState.purchaseValue.toDouble()
            } catch (e: Exception) {
                0.0
            }

            val saleValue: Double = try {
                currentState.saleValue.toDouble()
            } catch (e: Exception) {
                0.0
            }

            val lot = Lot(
                id = currentState.id,
                name = currentState.name,
                purchaseValue = purchaseValue,
                purchaseDate = currentState.purchaseDate,
                saleValue = saleValue,
                saleDate = currentState.saleDate,
                sold = false
            )

            if (lot.id == null) {
                val createdRespond = lotService.createLot(lot)
                if (createdRespond != FirestoreRespond.OK) {
                    _error.value = createdRespond
                } else {
                    _lotSaved.value = true
                }
            } else {
                val updateRespond = lotService.updateLot(lot)
                if (updateRespond != FirestoreRespond.OK) {
                    _error.value = updateRespond
                } else {
                    _lotSaved.value = true
                }
            }

        }
    }

}

data class LotFormState(
    val id: String? = null,
    val name: String = "",
    val purchaseValue: String = "",
    val purchaseDate: Timestamp = Timestamp.now(),
    val saleValue: String = "",
    val saleDate: Timestamp = Timestamp.now(),
    val sold: Boolean = false
)