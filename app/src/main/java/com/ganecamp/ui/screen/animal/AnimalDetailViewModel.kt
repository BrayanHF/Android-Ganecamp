package com.ganecamp.ui.screen.animal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.enums.ErrorType
import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.model.EventApplied
import com.ganecamp.domain.model.VaccineApplied
import com.ganecamp.domain.model.Weight
import com.ganecamp.domain.model.WeightValue
import com.ganecamp.domain.result.OperationResult.Error
import com.ganecamp.domain.result.OperationResult.Success
import com.ganecamp.domain.usecase.animal.CalculateAnimalAgeUseCase
import com.ganecamp.domain.usecase.animal.DeleteAnimalByIdUseCase
import com.ganecamp.domain.usecase.animal.GetAnimalByIdUseCase
import com.ganecamp.domain.usecase.eventapplied.GetEntityEventsUseCase
import com.ganecamp.domain.usecase.vaccineapplied.GetVaccinesAppliedUseCase
import com.ganecamp.domain.usecase.weight.GetAnimalWeightsUseCase
import com.ganecamp.domain.usecase.weight.LoadWeightValueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class AnimalDetailViewModel @Inject constructor(
    private val getAnimalByIdUseCase: GetAnimalByIdUseCase,
    private val calculateAnimalAgeUseCase: CalculateAnimalAgeUseCase,
    private val getVaccinesAppliedUseCase: GetVaccinesAppliedUseCase,
    private val getEntityEventsUseCase: GetEntityEventsUseCase,
    private val getAnimalWeightsUseCase: GetAnimalWeightsUseCase,
    private val loadWeightValueUseCase: LoadWeightValueUseCase,
    private val deleteAnimalByIdUseCase: DeleteAnimalByIdUseCase
) : ViewModel() {

    private val _animal = MutableStateFlow<Animal?>(null)
    val animal: StateFlow<Animal?> = _animal

    private val _vaccines = MutableStateFlow<List<VaccineApplied>>(emptyList())
    val vaccines: StateFlow<List<VaccineApplied>> = _vaccines

    private val _events = MutableStateFlow<List<EventApplied>>(emptyList())
    val events: StateFlow<List<EventApplied>> = _events

    private val _weights = MutableStateFlow<List<Weight>>(emptyList())
    val weights: StateFlow<List<Weight>> = _weights

    private val _ageAnimal = MutableStateFlow<Triple<Int, Int, Int>?>(null)
    val ageAnimal: StateFlow<Triple<Int, Int, Int>?> = _ageAnimal

    private val _weightValue = MutableStateFlow<WeightValue?>(null)
    val weightValue: StateFlow<WeightValue?> = _weightValue

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<ErrorType?>(null)
    val error: StateFlow<ErrorType?> = _error

    fun dismissError() {
        _error.value = null
    }

    fun loadAnimal(animalId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val animalRespond = getAnimalByIdUseCase(animalId)) {
                is Success -> {
                    _animal.value = animalRespond.data
                    calculateAge(animalRespond.data.birthDate)
                }

                is Error -> _error.value = animalRespond.errorType
            }
            _isLoading.value = false
        }
    }

    fun loadVaccines(animalId: String) {
        viewModelScope.launch {
            when (val vaccinesRespond = getVaccinesAppliedUseCase(animalId)) {
                is Success -> _vaccines.value = vaccinesRespond.data
                is Error -> _error.value = vaccinesRespond.errorType
            }
        }
    }

    fun loadEvents(animalId: String) {
        viewModelScope.launch {
            when (val eventsRespond = getEntityEventsUseCase(animalId, EntityType.Animal)) {
                is Success -> _events.value = eventsRespond.data
                is Error -> _error.value = eventsRespond.errorType
            }
        }
    }

    fun loadWeights(animalId: String) {
        viewModelScope.launch {
            when (val weightsRespond = getAnimalWeightsUseCase(animalId)) {
                is Success -> _weights.value = weightsRespond.data
                is Error -> _error.value = weightsRespond.errorType
            }
        }
    }

    fun loadWeightValue() {
        viewModelScope.launch {
            when (val weightValueRespond = loadWeightValueUseCase.invoke()) {
                is Success -> _weightValue.value = weightValueRespond.data
                is Error -> _error.value = weightValueRespond.errorType
            }
        }
    }

    fun deleteAnimal() {
        viewModelScope.launch {
            deleteAnimalByIdUseCase(_animal.value!!.id!!)
        }
    }

    private fun calculateAge(birthDate: Instant) {
        _ageAnimal.value = calculateAnimalAgeUseCase(birthDate)
    }

}