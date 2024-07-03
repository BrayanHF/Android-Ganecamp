package com.ganecamp.ui.lot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.model.LotDetail
import com.ganecamp.domain.services.LotService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class LotFormViewModel @Inject constructor(private val lotService: LotService) : ViewModel() {

    private val _uiState = MutableStateFlow(LotFormState())
    val uiState: StateFlow<LotFormState> = _uiState

    private val _lotSaved = MutableStateFlow(false)
    val lotSaved: StateFlow<Boolean> = _lotSaved

    fun loadLot(lotId: Int) {
        viewModelScope.launch {
            val lot = lotService.getLotById(lotId)
            _uiState.value = LotFormState(
                id = lot.id,
                purchaseValue = lot.purchaseValue.toString(),
                purchaseDate = lot.purchaseDate,
                saleValue = lot.saleValue.toString(),
                saleDate = lot.saleDate
            )
        }
    }

    fun onPurchaseValueChange(purchaseValue: String) {
        _uiState.value = _uiState.value.copy(purchaseValue = purchaseValue)
    }

    fun onPurchaseDateChange(purchaseDate: ZonedDateTime) {
        _uiState.value = _uiState.value.copy(purchaseDate = purchaseDate)
    }

    fun onSaleValueChange(saleValue: String) {
        _uiState.value = _uiState.value.copy(saleValue = saleValue)
    }

    fun onSaleDateChange(saleDate: ZonedDateTime) {
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

            val lot = LotDetail(
                id = currentState.id,
                purchaseValue = purchaseValue,
                purchaseDate = currentState.purchaseDate,
                saleValue = saleValue,
                saleDate = currentState.saleDate
            )

            if (lot.id == 0) {
                lotService.insertLot(lot)
            } else {
                lotService.updateLot(lot)
            }
            _lotSaved.value = true
        }
    }
}

data class LotFormState(
    val id: Int = 0,
    val purchaseValue: String = "",
    val purchaseDate: ZonedDateTime = ZonedDateTime.now(),
    val saleValue: String = "",
    val saleDate: ZonedDateTime = ZonedDateTime.now()
)