package com.ganecamp.ui.animal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.services.AnimalService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimalsViewModel @Inject constructor(private val animalService: AnimalService) : ViewModel() {

    private val _animals = MutableLiveData<List<Animal>>()
    val animals: LiveData<List<Animal>> = _animals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadAnimals()
    }

    fun loadAnimals() {
        _isLoading.value = true
        viewModelScope.launch {
            _animals.value = animalService.getAllAnimals()
            _isLoading.value = false
        }
    }
}