package com.ganecamp.ui.screen.animal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.services.AnimalService
import com.ganecamp.data.firibase.model.Animal
import com.ganecamp.domain.enums.RepositoryRespond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimalsViewModel @Inject constructor(private val animalService: AnimalService) : ViewModel() {

    private val _animals = MutableStateFlow<List<Animal>>(emptyList())
    val animals: StateFlow<List<Animal>> = _animals

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow(RepositoryRespond.OK)
    val error: StateFlow<RepositoryRespond> = _error

    fun loadAnimals() {
        viewModelScope.launch {
            _isLoading.value = true

            val animalRespond = animalService.getAllAnimals()
            if (animalRespond.second == RepositoryRespond.OK) {
                _animals.value = animalRespond.first
            } else {
                _error.value = animalRespond.second
            }
            _isLoading.value = false
        }
    }

}