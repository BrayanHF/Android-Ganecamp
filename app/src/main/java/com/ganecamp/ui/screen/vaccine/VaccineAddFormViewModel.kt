package com.ganecamp.ui.screen.vaccine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.enums.ErrorType
import com.ganecamp.domain.model.AnimalVaccine
import com.ganecamp.domain.model.Vaccine
import com.ganecamp.domain.result.OperationResult.Error
import com.ganecamp.domain.result.OperationResult.Success
import com.ganecamp.domain.usecase.vaccine.AddVaccineUseCase
import com.ganecamp.domain.usecase.vaccine.GetAllVaccinesUseCase
import com.ganecamp.domain.usecase.vaccineapplied.ApplyVaccineToAnimalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class VaccineAddFormViewModel @Inject constructor(
    private val getAllVaccinesUseCase: GetAllVaccinesUseCase,
    private val addVaccineUseCase: AddVaccineUseCase,
    private val applyVaccineToAnimalUseCase: ApplyVaccineToAnimalUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VaccineFormState())
    val uiState: StateFlow<VaccineFormState> = _uiState

    private val _vaccines = MutableStateFlow<List<Vaccine>>(emptyList())
    val vaccines: StateFlow<List<Vaccine>> = _vaccines

    private val _vaccineSaved = MutableStateFlow(false)
    val vaccineSaved: StateFlow<Boolean> = _vaccineSaved

    private val _error = MutableStateFlow<ErrorType?>(null)
    val error: StateFlow<ErrorType?> = _error

    fun dismissError() {
        _error.value = null
    }

    fun loadVaccines() {
        viewModelScope.launch {
            when (val result = getAllVaccinesUseCase()) {
                is Success -> _vaccines.value = result.data
                is Error -> _error.value = result.errorType
            }
        }
    }

    fun onVaccineSelected(vaccineId: String, vaccineName: String) {
        _uiState.value = _uiState.value.copy(vaccineId = vaccineId, name = vaccineName)
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onDescriptionChange(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun onDateChange(date: Instant) {
        _uiState.value = _uiState.value.copy(date = date)
    }

    fun onIsNewChange(isNew: Boolean) {
        _uiState.value = _uiState.value.copy(isNew = isNew)
    }

    fun saveVaccine() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState.isNew) {
                val newVaccine = Vaccine(
                    name = currentState.name, description = currentState.description
                )
                when (val result = addVaccineUseCase(newVaccine)) {
                    is Success -> currentState.vaccineId = result.data
                    is Error -> {
                        _error.value = result.errorType
                        return@launch
                    }
                }
            }

            val animalVaccine = AnimalVaccine(
                vaccineId = currentState.vaccineId, date = currentState.date
            )
            when (val result = applyVaccineToAnimalUseCase.invoke(
                animalId = currentState.animalId, animalVaccine = animalVaccine
            )) {
                is Success -> _vaccineSaved.value = true
                is Error -> _error.value = result.errorType
            }
        }
    }

    fun loadAnimalId(animalId: String) {
        _uiState.value = _uiState.value.copy(animalId = animalId)
    }

}

data class VaccineFormState(
    var vaccineId: String = "",
    val name: String = "",
    val description: String = "",
    val date: Instant = Instant.now(),
    val animalId: String = "",
    val isNew: Boolean = true
)