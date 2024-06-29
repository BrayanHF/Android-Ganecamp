package com.ganecamp.ui.animals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.model.AnimalDetail
import com.ganecamp.domain.model.Event
import com.ganecamp.domain.model.Vaccine
import com.ganecamp.domain.model.Weight
import com.ganecamp.domain.services.AnimalService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class AnimalDetailViewModel @Inject constructor(private val animalService: AnimalService) :
    ViewModel() {

    private val _animal = MutableLiveData<AnimalDetail>()
    val animal: LiveData<AnimalDetail> = _animal

    private val _vaccines = MutableLiveData<List<Vaccine>>()
    val vaccines: LiveData<List<Vaccine>> = _vaccines

    private val _weights = MutableLiveData<List<Weight>>()
    val weights: LiveData<List<Weight>> = _weights

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    private val _ageAnimal = MutableLiveData<Triple<Int, Int, Int>>()
    val ageAnimal: LiveData<Triple<Int, Int, Int>> get() = _ageAnimal

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadAnimal()
    }

    private fun loadAnimal() {
        viewModelScope.launch {
            _animal.value = animalService.getAnimalByTag("1")
        }
        calculateAge(animal.value!!.birthDate)
        _isLoading.value = false
    }

    private fun calculateAge(birthDate: ZonedDateTime) {
        _ageAnimal.value = animalService.calculateAge(birthDate)
    }

}