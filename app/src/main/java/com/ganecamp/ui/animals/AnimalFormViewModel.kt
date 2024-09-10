package com.ganecamp.ui.animals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.model.AnimalDetail
import com.ganecamp.domain.model.GeneralEvent
import com.ganecamp.domain.model.Weight
import com.ganecamp.domain.services.AnimalService
import com.ganecamp.domain.services.EventService
import com.ganecamp.domain.services.LotService
import com.ganecamp.domain.services.WeightService
import com.ganecamp.ui.general.formatNumber
import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
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

    private val _lots = MutableLiveData<List<Int>>()
    val lots: LiveData<List<Int>> = _lots

    private val _animalSaved = MutableStateFlow(false)
    val animalSaved: StateFlow<Boolean> = _animalSaved

    fun loadAnimal(animalId: Int) {
        viewModelScope.launch {
            val animal = animalService.getAnimalById(animalId)
            _uiState.value = AnimalFormState(
                id = animal.id,
                tag = animal.tag,
                gender = animal.gender,
                birthDate = animal.birthDate,
                purchaseValue = formatNumber(animal.purchaseValue.toString()),
                saleValue = formatNumber(animal.saleValue.toString()),
                state = animal.state,
                lotId = animalService.getLotById(animalId),
                weight = 0f.toString()
            )
        }
    }

    fun loadLots() {
        viewModelScope.launch {
            _lots.value = lotService.getAllLotsIDs()
        }
    }

    fun loadTag(tag: String) {
        _uiState.value = _uiState.value.copy(tag = tag)
    }

    fun onGenderChange(gender: Gender) {
        _uiState.value = _uiState.value.copy(gender = gender)
    }

    fun onBirthDateChange(birthDate: ZonedDateTime) {
        _uiState.value = _uiState.value.copy(birthDate = birthDate)
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

    fun onLotChange(lotId: Int) {
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

            val animal = AnimalDetail(
                id = currentState.id,
                tag = currentState.tag,
                gender = currentState.gender,
                birthDate = currentState.birthDate,
                purchaseValue = purchaseValue,
                saleValue = saleValue,
                state = currentState.state
            )

            if (animal.id == 0) {
                animalService.insertAnimal(animal)
                val newId = animalService.getIdByTag(animal.tag)
                if (currentState.lotId != 0) animalService.addLotToAnimal(newId, currentState.lotId)
                weightService.insertWeight(Weight(0, newId, weight, ZonedDateTime.now()))
                eventService.addEventToAnimal(GeneralEvent(0, newId, 1, ZonedDateTime.now()))
                _uiState.value = _uiState.value.copy(id = newId)
            } else {
                val lotId = animalService.getLotById(currentState.id)
                animalService.updateAnimal(animal)
                if (currentState.lotId == 0) {
                    animalService.removeFromLot(currentState.id)
                } else {
                    if (currentState.lotId != lotId && lotId != 0) {
                        animalService.changeLotToAnimal(currentState.id, currentState.lotId)
                    } else {
                        animalService.addLotToAnimal(currentState.id, currentState.lotId)
                    }
                }
            }
            _animalSaved.value = true
        }
    }
}

data class AnimalFormState(
    val id: Int = 0,
    val tag: String = "",
    val gender: Gender = Gender.Male,
    val birthDate: ZonedDateTime = ZonedDateTime.now(),
    val purchaseValue: String = "",
    val saleValue: String = "",
    val state: State = State.Healthy,
    val lotId: Int = 0,
    val weight: String = ""
)