package com.ganecamp.ui.screen.lot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.data.firibase.model.Animal
import com.ganecamp.data.firibase.model.EventApplied
import com.ganecamp.data.firibase.model.Lot
import com.ganecamp.domain.services.EventAppliedService
import com.ganecamp.domain.services.LotService
import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.enums.RepositoryRespond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LotDetailViewModel @Inject constructor(
    private val lotService: LotService,
    private val eventAppliedService: EventAppliedService,
) : ViewModel() {

    private val _lot = MutableStateFlow<Lot?>(null)
    val lot: StateFlow<Lot?> = _lot

    private val _events = MutableStateFlow<List<EventApplied>>(emptyList())
    val events: StateFlow<List<EventApplied>> = _events

    private val _animals = MutableStateFlow<List<Animal>>(emptyList())
    val animals: StateFlow<List<Animal>> = _animals

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow(RepositoryRespond.OK)
    val error: StateFlow<RepositoryRespond> = _error

    fun loadLot(lotId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val (lot, repositoryRespond) = lotService.getLotById(lotId)
            if (repositoryRespond == RepositoryRespond.OK) {
                _lot.value = lot

            } else {
                _error.value = repositoryRespond
            }
            _isLoading.value = false
        }
    }

    fun loadEvents(lotId: String) {
        viewModelScope.launch {
            val eventsRespond = eventAppliedService.getEntityEvents(lotId, EntityType.Lot)
            if (eventsRespond.second == RepositoryRespond.OK) {
                _events.value = eventsRespond.first
            } else {
                _error.value = eventsRespond.second
            }
        }
    }

    fun loadAnimals(lotId: String) {
        viewModelScope.launch {
            val animalsRespond = lotService.getAnimalsByLotId(lotId)
            if (animalsRespond.second == RepositoryRespond.OK) {
                _animals.value = animalsRespond.first
            } else {
                _error.value = animalsRespond.second
            }
        }
    }

    fun deleteLot(lotId: String) {
        viewModelScope.launch {
            lotService.deleteLotById(lotId)
        }
    }

}