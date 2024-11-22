package com.ganecamp.ui.screen.lot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.enums.ErrorType
import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.model.EventApplied
import com.ganecamp.domain.model.Lot
import com.ganecamp.domain.result.OperationResult.Error
import com.ganecamp.domain.result.OperationResult.Success
import com.ganecamp.domain.usecase.eventapplied.GetEntityEventsUseCase
import com.ganecamp.domain.usecase.lot.DeleteLotByIdUseCase
import com.ganecamp.domain.usecase.lot.GetAnimalsByLotIdUseCase
import com.ganecamp.domain.usecase.lot.GetLotByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LotDetailViewModel @Inject constructor(
    private val getLotByIdUseCase: GetLotByIdUseCase,
    private val getEntityEventsUseCase: GetEntityEventsUseCase,
    private val getAnimalByLodIdUseCase: GetAnimalsByLotIdUseCase,
    private val deleteLotByIdUseCase: DeleteLotByIdUseCase
) : ViewModel() {

    private val _lot = MutableStateFlow<Lot?>(null)
    val lot: StateFlow<Lot?> = _lot

    private val _events = MutableStateFlow<List<EventApplied>>(emptyList())
    val events: StateFlow<List<EventApplied>> = _events

    private val _animals = MutableStateFlow<List<Animal>>(emptyList())
    val animals: StateFlow<List<Animal>> = _animals

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<ErrorType?>(null)
    val error: StateFlow<ErrorType?> = _error

    fun dismissError() {
        _error.value = null
    }

    fun loadLot(lotId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = getLotByIdUseCase(lotId)) {
                is Success -> _lot.value = result.data
                is Error -> _error.value = result.errorType
            }
            _isLoading.value = false
        }
    }

    fun loadEvents(lotId: String) {
        viewModelScope.launch {
            when (val result = getEntityEventsUseCase(lotId, EntityType.Lot)) {
                is Success -> _events.value = result.data
                is Error -> _error.value = result.errorType
            }
        }
    }

    fun loadAnimals(lotId: String) {
        viewModelScope.launch {
            when (val result = getAnimalByLodIdUseCase(lotId)) {
                is Success -> _animals.value = result.data
                is Error -> _error.value = result.errorType
            }
        }
    }

    fun deleteLot() {
        viewModelScope.launch {
            deleteLotByIdUseCase(lot.value!!.id!!)
        }
    }

}