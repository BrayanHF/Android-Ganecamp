package com.ganecamp.ui.screen.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.enums.ErrorType
import com.ganecamp.domain.model.Weight
import com.ganecamp.domain.result.OperationResult.Error
import com.ganecamp.domain.result.OperationResult.Success
import com.ganecamp.domain.usecase.weight.AddWeightUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class WeightFormViewModel @Inject constructor(private val addWeightUseCase: AddWeightUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow(WeightFormState())
    val uiState: StateFlow<WeightFormState> = _uiState

    private val _weightSaved = MutableStateFlow(false)
    val weightSaved: StateFlow<Boolean> = _weightSaved

    private val _error = MutableStateFlow<ErrorType?>(null)
    val error: StateFlow<ErrorType?> = _error

    fun dismissError() {
        _error.value = null
    }

    fun loadAnimalId(animalId: String) {
        _uiState.value = _uiState.value.copy(animalId = animalId)
    }

    fun onWeightChange(weight: String) {
        _uiState.value = _uiState.value.copy(weight = weight)
    }

    fun onDateChange(date: Instant) {
        _uiState.value = _uiState.value.copy(date = date)
    }

    fun saveWeight() {
        val currentSate = _uiState.value
        viewModelScope.launch {
            val newWeight = Weight(weight = currentSate.weight.toFloat(), date = currentSate.date)
            when (val result = addWeightUseCase(currentSate.animalId, newWeight)) {
                is Success -> _weightSaved.value = true
                is Error -> _error.value = result.errorType
            }
        }
    }

}

data class WeightFormState(
    val animalId: String = "", val weight: String = "", val date: Instant = Instant.now()
)