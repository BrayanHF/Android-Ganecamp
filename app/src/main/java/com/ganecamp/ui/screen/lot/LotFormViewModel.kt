package com.ganecamp.ui.screen.lot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.enums.ErrorType
import com.ganecamp.domain.model.Lot
import com.ganecamp.domain.result.OperationResult.Error
import com.ganecamp.domain.result.OperationResult.Success
import com.ganecamp.domain.usecase.lot.AddLotUseCase
import com.ganecamp.domain.usecase.lot.GetLotByIdUseCase
import com.ganecamp.domain.usecase.lot.UpdateLotUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class LotFormViewModel @Inject constructor(
    private val getLotByIdUseCase: GetLotByIdUseCase,
    private val addLotUseCase: AddLotUseCase,
    private val updateLotUseCase: UpdateLotUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LotFormState())
    val uiState: StateFlow<LotFormState> = _uiState

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _lotSaved = MutableStateFlow(false)
    val lotSaved: StateFlow<Boolean> = _lotSaved

    private val _error = MutableStateFlow<ErrorType?>(null)
    val error: StateFlow<ErrorType?> = _error

    fun dismissError() {
        _error.value = null
    }

    fun nothingToLoad() {
        _isLoading.value = false
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

    fun loadLot(lotId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val respond = getLotByIdUseCase(lotId)) {
                is Success -> {
                    val lot = respond.data
                    _uiState.value = LotFormState(
                        id = lot.id,
                        name = lot.name,
                        purchasedAnimals = lot.purchasedAnimals.toString(),
                        purchaseValue = lot.purchaseValue.toString(),
                        purchaseDate = lot.purchaseDate,
                        saleValue = lot.saleValue.toString(),
                        saleDate = lot.saleDate,
                        sold = lot.sold
                    )
                }

                is Error -> _error.value = respond.errorType

            }
            _isLoading.value = false
        }
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
                purchaseDate = currentState.purchaseDate,
                saleValue = convertNumberSafely(currentState.saleValue),
                saleDate = currentState.saleDate,
                sold = currentState.sold
            )

            if (lot.id == null) {
                when (val respond = addLotUseCase(lot)) {
                    is Success -> {
                        _uiState.value = _uiState.value.copy(id = respond.data)
                        _lotSaved.value = true
                    }

                    is Error -> _error.value = respond.errorType
                }
            } else {
                when (val respond = updateLotUseCase(lot)) {
                    is Success -> _lotSaved.value = true
                    is Error -> _error.value = respond.errorType
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