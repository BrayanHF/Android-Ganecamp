package com.ganecamp.ui.lot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.services.EventAppliedService
import com.ganecamp.domain.services.LotService
import com.ganecamp.model.objects.Animal
import com.ganecamp.model.objects.EventApplied
import com.ganecamp.model.objects.Lot
import com.ganecamp.utilities.enums.EntityType
import com.ganecamp.utilities.enums.FirestoreRespond
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

    private val _error = MutableStateFlow(FirestoreRespond.OK)
    val error: StateFlow<FirestoreRespond> = _error

    fun loadLot(lotId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val lotRespond = lotService.getLotById(lotId)
            if (lotRespond.second == FirestoreRespond.OK) {
                _lot.value = lotRespond.first

            } else {
                _error.value = lotRespond.second
            }
            _isLoading.value = false
        }
    }

    fun loadEvents(lotId: String) {
        viewModelScope.launch {
            val eventsRespond = eventAppliedService.getEntityEvents(lotId, EntityType.Lot)
            if (eventsRespond.second == FirestoreRespond.OK) {
                _events.value = eventsRespond.first
            } else {
                _error.value = eventsRespond.second
            }
        }
    }

    fun loadAnimals(lotId: String) {
        viewModelScope.launch {
            val animalsRespond = lotService.getAnimalsByLotId(lotId)
            if (animalsRespond.second == FirestoreRespond.OK) {
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