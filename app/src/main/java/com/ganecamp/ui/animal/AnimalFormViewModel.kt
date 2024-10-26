package com.ganecamp.ui.animal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.services.AnimalService
import com.ganecamp.domain.services.EventService
import com.ganecamp.domain.services.LotService
import com.ganecamp.domain.services.WeightService
import com.ganecamp.model.objects.Animal
import com.ganecamp.model.objects.Lot
import com.ganecamp.model.objects.Weight
import com.ganecamp.ui.general.formatNumber
import com.ganecamp.utilities.enums.FirestoreRespond
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AnimalFormViewModel @Inject constructor(
    private val animalService: AnimalService,
    private val lotService: LotService,
    private val weightService: WeightService,
    private val eventService: EventService
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnimalFormState())
    val uiState: StateFlow<AnimalFormState> = _uiState

    private val _lots = MutableStateFlow<List<Lot>>(emptyList())
    val lots: StateFlow<List<Lot>> = _lots

    private val _animalSaved = MutableStateFlow(false)
    val animalSaved: StateFlow<Boolean> = _animalSaved

    private val _error = MutableStateFlow(FirestoreRespond.OK)
    val error: StateFlow<FirestoreRespond> = _error


    fun loadAnimal(animalId: String) {
        viewModelScope.launch {
            val animalResponse = animalService.getAnimalById(animalId)
            if (animalResponse.second != FirestoreRespond.OK) {
                animalResponse.first?.let { animal ->
                    _uiState.value = AnimalFormState(
                        id = animal.id,
                        tag = animal.tag,
                        gender = animal.gender,
                        birthDate = animal.birthDate,
                        purchaseValue = formatNumber(animal.purchaseValue.toString()),
                        saleValue = formatNumber(animal.saleValue.toString()),
                        state = animal.state,
                        lotId = animal.lotId,
                        weight = 0f.toString()
                    )
                }
            } else {
                _error.value = animalResponse.second
            }
        }
    }

    fun loadLots() {
        viewModelScope.launch {
            val lotResponse = lotService.getAllLots()
            if (lotResponse.second != FirestoreRespond.OK) {
                _lots.value = lotResponse.first
            } else {
                _error.value = lotResponse.second
            }
        }
    }

    fun loadTag(tag: String) {
        _uiState.value = _uiState.value.copy(tag = tag)
    }

    fun onGenderChange(gender: Gender) {
        _uiState.value = _uiState.value.copy(gender = gender)
    }

    fun onBirthDateChange(birthDate: Instant) {
        _uiState.value = _uiState.value.copy(birthDate = Timestamp(Date.from(birthDate)))
    }

    fun onPurchaseValueChange(purchaseValue: String) {
        _uiState.value = _uiState.value.copy(purchaseValue = purchaseValue)
    }

    fun onSaleValueChange(saleValue: String) {
        _uiState.value = _uiState.value.copy(saleValue = saleValue)
    }

    fun onStateChange(state: State) {
        _uiState.value = _uiState.value.copy(state = state)
    }

    fun onWeightChange(weight: String) {
        _uiState.value = _uiState.value.copy(weight = weight)
    }

    fun onLotChange(lotId: String?) {
        _uiState.value = _uiState.value.copy(lotId = lotId)
    }

    fun saveAnimal() {
        viewModelScope.launch {
            val currentState = _uiState.value

            val weight: Float = try {
                currentState.weight.toFloat()
            } catch (e: Exception) {
                0f
            }

            val purchaseValue: Double = try {
                currentState.purchaseValue.toDouble()
            } catch (e: Exception) {
                0.0
            }

            val saleValue: Double = try {
                currentState.saleValue.toDouble()
            } catch (e: Exception) {
                0.0
            }

            val animal = Animal(
                id = currentState.id,
                tag = currentState.tag,
                gender = currentState.gender,
                birthDate = currentState.birthDate,
                lotId = currentState.lotId,
                purchaseValue = purchaseValue,
                purchaseDate = Timestamp.now(),
                saleValue = saleValue,
                saleDate = Timestamp.now(),
                state = currentState.state
            )

            if (animal.id == null) {
                val createdRespond = animalService.createAnimal(animal)
                if (createdRespond == FirestoreRespond.OK) {
                    val newAnimalRespond = animalService.getAnimalByTag(currentState.tag)
                    if (newAnimalRespond.second == FirestoreRespond.OK) {
                        newAnimalRespond.first?.let { newAnimal ->
                            weightService.createWeight(
                                newAnimal.id!!, Weight(weight = weight, date = Timestamp.now())
                            )
                            _uiState.value = _uiState.value.copy(id = newAnimal.id!!)
                            _animalSaved.value = true
                        }
                    } else {
                        _error.value = newAnimalRespond.second
                    }
                } else {
                    _error.value = createdRespond
                }
            } else {
                val updateRespond = animalService.updateAnimal(animal)
                if (updateRespond == FirestoreRespond.OK) {
                    _animalSaved.value = true
                } else {
                    _error.value = updateRespond
                }
            }
        }
    }

}

data class AnimalFormState(
    val id: String? = null,
    val tag: String = "",
    val gender: Gender = Gender.Male,
    val birthDate: Timestamp = Timestamp.now(),
    val purchaseValue: String = "",
    val saleValue: String = "",
    val state: State = State.Healthy,
    val lotId: String? = null,
    val weight: String = ""
)