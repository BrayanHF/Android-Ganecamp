package com.ganecamp.ui.animals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.model.AnimalDetail
import com.ganecamp.domain.model.Description
import com.ganecamp.domain.model.Weight
import com.ganecamp.domain.services.AnimalService
import com.ganecamp.domain.services.EventService
import com.ganecamp.domain.services.VaccineService
import com.ganecamp.domain.services.WeightService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class AnimalDetailViewModel @Inject constructor(
    private val animalService: AnimalService,
    private val eventService: EventService,
    private val vaccineService: VaccineService,
    private val weightService: WeightService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animalId: Int = savedStateHandle["animalId"] ?: 0

    private val _animal = MutableLiveData<AnimalDetail>()
    val animal: LiveData<AnimalDetail> = _animal

    private val _lotId = MutableLiveData<Int>()
    val lotId: LiveData<Int> = _lotId

    private val _vaccines = MutableLiveData<List<Description>>()
    val vaccines: LiveData<List<Description>> = _vaccines

    private val _weights = MutableLiveData<List<Weight>>()
    val weights: LiveData<List<Weight>> = _weights

    private val _events = MutableLiveData<List<Description>>()
    val events: LiveData<List<Description>> = _events

    private val _ageAnimal = MutableLiveData<Triple<Int, Int, Int>>()
    val ageAnimal: LiveData<Triple<Int, Int, Int>> get() = _ageAnimal

    private val _weightValue = MutableLiveData<Float>()
    val weightValue: LiveData<Float> = _weightValue

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadWeights() {
        viewModelScope.launch {
            val weights = weightService.animalWeights(animalId)
            _weights.value = weights
        }
    }

    fun loadEvents() {
        viewModelScope.launch {
            val events = eventService.animalEvents(animalId)
            _events.value = events
        }
    }

    fun loadVaccines() {
        viewModelScope.launch {
            val vaccines = vaccineService.animalVaccines(animalId)
            _vaccines.value = vaccines
        }
    }

    fun loadLotId() {
        viewModelScope.launch {
            val lotId = animalService.getLotById(animalId)
            _lotId.value = lotId
        }
    }

    fun loadAnimal() {
        viewModelScope.launch {
            val animal = animalService.getAnimalById(animalId)
            _animal.value = animal
            calculateAge(animal.birthDate)
            _isLoading.value = false
        }
    }

    fun loadWeightValue() {
        // Todo: Implement this function where the weight value is in an external service
        //  and in the offline case it has a local value
    }

    private fun calculateAge(birthDate: ZonedDateTime) {
        _ageAnimal.value = animalService.calculateAge(birthDate)
    }

    fun deleteAnimal(animalId: Int) {
        viewModelScope.launch {
            animalService.deleteAnimalById(animalId)
        }
    }

}