package com.ganecamp.ui.screen.animal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.services.AnimalService
import com.ganecamp.domain.services.EventAppliedService
import com.ganecamp.domain.services.VaccineAppliedService
import com.ganecamp.domain.services.WeightService
import com.ganecamp.data.firibase.model.Animal
import com.ganecamp.data.firibase.model.EventApplied
import com.ganecamp.data.firibase.model.VaccineApplied
import com.ganecamp.data.firibase.model.Weight
import com.ganecamp.data.firibase.model.WeightValue
import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.enums.RepositoryRespond
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimalDetailViewModel @Inject constructor(
    private val animalService: AnimalService,
    private val weightService: WeightService,
    private val vaccineAppliedService: VaccineAppliedService,
    private val eventAppliedService: EventAppliedService,
) : ViewModel() {

    private val _animal = MutableStateFlow<Animal?>(null)
    val animal: StateFlow<Animal?> = _animal

    private val _vaccines = MutableStateFlow<List<VaccineApplied>>(emptyList())
    val vaccines: StateFlow<List<VaccineApplied>> = _vaccines

    private val _weights = MutableStateFlow<List<Weight>>(emptyList())
    val weights: StateFlow<List<Weight>> = _weights

    private val _events = MutableStateFlow<List<EventApplied>>(emptyList())
    val events: StateFlow<List<EventApplied>> = _events

    private val _ageAnimal = MutableStateFlow<Triple<Int, Int, Int>?>(null)
    val ageAnimal: StateFlow<Triple<Int, Int, Int>?> = _ageAnimal

    private val _weightValue = MutableStateFlow<WeightValue?>(null)
    val weightValue: StateFlow<WeightValue?> = _weightValue

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow(RepositoryRespond.OK)
    val error: StateFlow<RepositoryRespond> = _error

    fun loadWeights(animalId: String) {
        viewModelScope.launch {
            val weightRespond = weightService.getAnimalWeights(animalId)
            if (weightRespond.second == RepositoryRespond.OK) {
                _weights.value = weightRespond.first
            } else {
                _error.value = weightRespond.second
            }
        }
    }

    fun loadEvents(animalId: String) {
        viewModelScope.launch {
            val eventRespond = eventAppliedService.getEntityEvents(animalId, EntityType.Animal)
            if (eventRespond.second == RepositoryRespond.OK) {
                _events.value = eventRespond.first
            } else {
                _error.value = eventRespond.second
            }
        }
    }

    fun loadVaccines(animalId: String) {
        viewModelScope.launch {
            val vaccinesRespond = vaccineAppliedService.getVaccinesApplied(animalId)
            if (vaccinesRespond.second == RepositoryRespond.OK) {
                _vaccines.value = vaccinesRespond.first
            } else {
                _error.value = vaccinesRespond.second
            }
        }
    }

    fun loadAnimal(animalId: String) {
        viewModelScope.launch {
            val animalRespond = animalService.getAnimalById(animalId)
            if (animalRespond.second == RepositoryRespond.OK) {
                _animal.value = animalRespond.first
                animalRespond.first?.let {
                    calculateAge(it.birthDate)
                }
            } else {
                _error.value = animalRespond.second
            }
            _isLoading.value = false
        }
    }

    private fun calculateAge(birthDate: Timestamp) {
        _ageAnimal.value = animalService.calculateAge(birthDate)
    }

    fun deleteAnimal(tag: String) {
        viewModelScope.launch {
            animalService.deleteAnimalByTag(tag)
        }
    }

    fun loadWeightValue() {
        viewModelScope.launch {
            val weightRespond = weightService.loadWeightValue()
            if (weightRespond.second == RepositoryRespond.OK) {
                _weightValue.value = weightRespond.first
            } else {
                _error.value = weightRespond.second
            }
        }
    }

}