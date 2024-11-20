package com.ganecamp.ui.screen.lot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.data.firibase.model.Lot
import com.ganecamp.domain.enums.RepositoryRespond
import com.ganecamp.domain.services.LotService
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

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _lotSaved = MutableStateFlow(false)
    val lotSaved: StateFlow<Boolean> = _lotSaved

    private val _error = MutableStateFlow(RepositoryRespond.OK)
    val error: StateFlow<RepositoryRespond> = _error

    fun nothingToLoad() {
        _isLoading.value = false
    }

    fun loadLot(lotId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val (lot, respond) = lotService.getLotById(lotId)
            if (respond == RepositoryRespond.OK) {
                lot?.let { lotFound ->
                    _uiState.value = LotFormState(
                        id = lotFound.id,
                        name = lotFound.name,
                        purchasedAnimals = lotFound.purchasedAnimals.toString(),
                        purchaseValue = lotFound.purchaseValue.toString(),
                        purchaseDate = lotFound.purchaseDate.toInstant(),
                        saleValue = lotFound.saleValue.toString(),
                        saleDate = lotFound.saleDate.toInstant(),
                        sold = lotFound.sold
                    )
                }
                _isLoading.value = false
            } else {
                _error.value = respond
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
                if (respond == RepositoryRespond.OK) {
                    _lotSaved.value = true
                    _uiState.value = _uiState.value.copy(id = newLotId)
                } else {
                    _error.value = respond
                }
            } else {
                val updateRespond = lotService.updateLot(lot)
                if (updateRespond == RepositoryRespond.OK) {
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