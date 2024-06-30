package com.ganecamp.ui.lot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.model.Description
import com.ganecamp.domain.model.LotDetail
import com.ganecamp.domain.services.EventService
import com.ganecamp.domain.services.LotService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LotDetailViewModel @Inject constructor(
    private val lotService: LotService,
    private val eventService: EventService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val lotId: Int = savedStateHandle["lotId"] ?: 0

    private val _lot = MutableLiveData<LotDetail>()
    val lot: LiveData<LotDetail> = _lot

    private val _events = MutableLiveData<List<Description>>()
    val events: LiveData<List<Description>> = _events

    private val _animals = MutableLiveData<List<Animal>>()
    val animals: LiveData<List<Animal>> = _animals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadLot()
        loadEvents()
        loadAnimals()
    }

    private fun loadLot() {
        _isLoading.value = true
        viewModelScope.launch {
            val lot = lotService.getLotById(lotId)
            _lot.value = lot
            _isLoading.value = false
        }
    }

    private fun loadEvents() {
        viewModelScope.launch {
            val events = eventService.lotEvents(lotId)
            _events.value = events
        }
    }

    private fun loadAnimals() {
        viewModelScope.launch {
            val animals = lotService.getAnimalsByLotId(lotId)
            _animals.value = animals
        }
    }

}
