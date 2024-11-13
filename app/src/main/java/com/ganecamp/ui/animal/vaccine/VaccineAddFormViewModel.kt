package com.ganecamp.ui.animal.vaccine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.data.firibase.model.AnimalVaccine
import com.ganecamp.data.firibase.model.Vaccine
import com.ganecamp.domain.services.VaccineAppliedService
import com.ganecamp.domain.services.VaccineService
import com.ganecamp.utilities.enums.FirestoreRespond
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class VaccineAddFormViewModel @Inject constructor(
    private val vaccineService: VaccineService,
    private val vaccineAppliedService: VaccineAppliedService
) : ViewModel() {

    private val _uiState = MutableStateFlow(VaccineFormState())
    val uiState: StateFlow<VaccineFormState> = _uiState

    private val _vaccines = MutableStateFlow<List<Vaccine>>(emptyList())
    val vaccines: StateFlow<List<Vaccine>> = _vaccines

    private val _vaccineSaved = MutableStateFlow(false)
    val vaccineSaved: StateFlow<Boolean> = _vaccineSaved

    private val _error = MutableStateFlow(FirestoreRespond.OK)
    val error: StateFlow<FirestoreRespond> = _error

    fun loadVaccines() {
        viewModelScope.launch {
            val vaccineResponse = vaccineService.getAllVaccines()
            if (vaccineResponse.second == FirestoreRespond.OK) {
                _vaccines.value = vaccineResponse.first
            } else {
                _error.value = vaccineResponse.second
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
                val vaccineResponse = vaccineService.createVaccine(
                    Vaccine(
                        name = currentState.name, description = currentState.description
                    )
                )
                if (vaccineResponse.second == FirestoreRespond.OK) {
                    currentState.vaccineId = vaccineResponse.first!!
                } else {
                    _error.value = vaccineResponse.second
                    return@launch
                }
            }
            val applyVaccineResponse = vaccineAppliedService.applyVaccineAnimal(
                currentState.animalId, AnimalVaccine(
                    vaccineId = currentState.vaccineId, date = Timestamp(currentState.date)
                )
            )

            if (applyVaccineResponse == FirestoreRespond.OK) {
                _vaccineSaved.value = true
            } else {
                _error.value = applyVaccineResponse
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
    val isNew: Boolean = false
)