package com.ganecamp.ui.animal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.data.firibase.model.Animal
import com.ganecamp.data.firibase.model.Lot
import com.ganecamp.data.firibase.model.Weight
import com.ganecamp.domain.services.AnimalService
import com.ganecamp.domain.services.LotService
import com.ganecamp.domain.services.WeightService
import com.ganecamp.ui.general.formatNumber
import com.ganecamp.utilities.enums.Breed
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
            if (animalResponse.second == FirestoreRespond.OK) {
                animalResponse.first?.let { animal ->
                    _uiState.value = AnimalFormState(
                        id = animal.id,
                        tag = animal.tag,
                        nickname = animal.nickname,
                        lotId = animal.lotId,
                        gender = animal.gender,
                        breed = animal.breed,
                        birthDate = animal.birthDate,
                        purchaseValue = formatNumber(animal.purchaseValue.toString()),
                        purchaseDate = animal.purchaseDate,
                        saleValue = formatNumber(animal.saleValue.toString()),
                        saleDate = animal.saleDate,
                        state = animal.state
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
            if (lotResponse.second == FirestoreRespond.OK) {
                _lots.value = lotResponse.first
            } else {
                _error.value = lotResponse.second
            }
        }
    }

    fun loadTag(tag: String) {
        _uiState.value = _uiState.value.copy(tag = tag)
    }

    fun onNicknameChange(nickname: String?) {
        _uiState.value = _uiState.value.copy(nickname = nickname)
    }

    fun onLotChange(lotId: String?) {
        _uiState.value = _uiState.value.copy(lotId = lotId)
    }

    fun onGenderChange(gender: Gender) {
        _uiState.value = _uiState.value.copy(gender = gender)
    }

    fun onBreedChange(breed: Breed) {
        _uiState.value = _uiState.value.copy(breed = breed)
    }

    fun onBirthDateChange(birthDate: Instant) {
        _uiState.value = _uiState.value.copy(birthDate = Timestamp(Date.from(birthDate)))
    }

    fun onPurchaseValueChange(purchaseValue: String) {
        _uiState.value = _uiState.value.copy(purchaseValue = purchaseValue)
    }

    fun onPurchaseDateChange(purchaseDate: Instant) {
        _uiState.value = _uiState.value.copy(purchaseDate = Timestamp(Date.from(purchaseDate)))
    }

    fun onSaleValueChange(saleValue: String) {
        _uiState.value = _uiState.value.copy(saleValue = saleValue)
    }

    fun onSaleDateChange(saleDate: Instant) {
        _uiState.value = _uiState.value.copy(saleDate = Timestamp(Date.from(saleDate)))
    }

    fun onStateChange(state: State) {
        _uiState.value = _uiState.value.copy(state = state)
    }

    fun onWeightChange(weight: String) {
        _uiState.value = _uiState.value.copy(weight = weight)
    }

    private fun convertToDouble(numString: String): Double {
        return try {
            numString.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    fun saveAnimal() {
        viewModelScope.launch {
            val currentState = _uiState.value

            val weight: Float = convertToDouble(currentState.weight).toFloat()

            val purchaseValue: Double = convertToDouble(currentState.purchaseValue)

            val saleValue: Double = convertToDouble(currentState.saleValue)

            val animal = Animal(
                id = currentState.id,
                tag = currentState.tag,
                nickname = currentState.nickname,
                lotId = currentState.lotId,
                gender = currentState.gender,
                breed = currentState.breed,
                birthDate = currentState.birthDate,
                purchaseValue = purchaseValue,
                purchaseDate = currentState.purchaseDate,
                saleValue = saleValue,
                saleDate = currentState.saleDate,
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
    val nickname: String? = null,
    val lotId: String? = null,
    val gender: Gender = Gender.Male,
    val breed: Breed = Breed.ZEBU,
    val birthDate: Timestamp = Timestamp.now(),
    val purchaseValue: String = "",
    val purchaseDate: Timestamp = Timestamp.now(),
    val saleValue: String = "",
    val saleDate: Timestamp = Timestamp.now(),
    val state: State = State.Healthy,
    val weight: String = ""
)