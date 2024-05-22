package com.ganecamp.ui.animals

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.model.AnimalDetail
import com.ganecamp.domain.services.AnimalService
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class AnimalsViewModel @Inject constructor(private val animalService: AnimalService) : ViewModel() {

    private val _animals = MutableLiveData<List<Animal>>()
    val animals: LiveData<List<Animal>> = _animals

    init {
        loadAnimals()
    }

    private fun loadAnimals() {

        viewModelScope.launch {
            animalService.deleteAllAnimals()

            animalService.insertAnimal(
                AnimalDetail(
                    "1", "1", Gender.Male, ZonedDateTime.now(), 0.0, 0.0, State.Healthy
                )
            )
            animalService.insertAnimal(
                AnimalDetail(
                    "2", "2", Gender.Male, ZonedDateTime.now(), 0.0, 0.0, State.Healthy
                )
            )
            animalService.insertAnimal(
                AnimalDetail(
                    "3", "3", Gender.Male, ZonedDateTime.now(), 0.0, 0.0, State.Healthy
                )
            )

            _animals.value = animalService.getAllAnimals()
        }
    }
}