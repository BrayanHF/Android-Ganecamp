package com.ganecamp.ui.screen.animal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.enums.ErrorType
import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.result.OperationResult.Error
import com.ganecamp.domain.result.OperationResult.Success
import com.ganecamp.domain.usecase.animal.GetAllAnimalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimalsViewModel @Inject constructor(
    private val getAllAnimalsUseCase: GetAllAnimalsUseCase
) : ViewModel() {

    private val _animals = MutableStateFlow<List<Animal>>(emptyList())
    val animals: StateFlow<List<Animal>> = _animals

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<ErrorType?>(null)
    val error: StateFlow<ErrorType?> = _error

    fun dismissError() {
        _error.value = null
    }

    fun loadAnimals() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = getAllAnimalsUseCase()) {
                is Success -> _animals.value = result.data
                is Error -> _error.value = result.errorType
            }
            _isLoading.value = false
        }
    }

}