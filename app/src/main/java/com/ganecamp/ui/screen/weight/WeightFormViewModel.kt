package com.ganecamp.ui.screen.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.data.firibase.model.Weight
import com.ganecamp.domain.services.WeightService
import com.ganecamp.domain.enums.RepositoryRespond
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class WeightFormViewModel @Inject constructor(private val weightService: WeightService) :
    ViewModel() {

    private val _uiState = MutableStateFlow(WeightFormState())
    val uiState: StateFlow<WeightFormState> = _uiState

    private val _weightSaved = MutableStateFlow(false)
    val weightSaved: StateFlow<Boolean> = _weightSaved

    private val _error = MutableStateFlow(RepositoryRespond.OK)
    val error: StateFlow<RepositoryRespond> = _error

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
            val weightResponse = weightService.createWeight(
                currentSate.animalId, Weight(
                    weight = currentSate.weight.toFloat(), date = Timestamp(currentSate.date)
                )
            )

            if (weightResponse == RepositoryRespond.OK) {
                _weightSaved.value = true
            } else {
                _error.value = weightResponse
            }
        }
    }

}

data class WeightFormState(
    val animalId: String = "", val weight: String = "", val date: Instant = Instant.now()
)