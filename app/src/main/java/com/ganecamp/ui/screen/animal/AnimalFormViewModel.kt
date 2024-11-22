package com.ganecamp.ui.screen.animal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.enums.AnimalBreed
import com.ganecamp.domain.enums.AnimalGender
import com.ganecamp.domain.enums.AnimalState
import com.ganecamp.domain.enums.ErrorType
import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.model.Lot
import com.ganecamp.domain.model.Weight
import com.ganecamp.domain.result.OperationResult.Error
import com.ganecamp.domain.result.OperationResult.Success
import com.ganecamp.domain.usecase.animal.AddAnimalUseCase
import com.ganecamp.domain.usecase.animal.GetAnimalByIdUseCase
import com.ganecamp.domain.usecase.animal.UpdateAnimalUseCase
import com.ganecamp.domain.usecase.lot.GetAllLotsUseCase
import com.ganecamp.ui.util.formatNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class AnimalFormViewModel @Inject constructor(
    private val getAnimalByIdUseCase: GetAnimalByIdUseCase,
    private val getAllLotsUseCase: GetAllLotsUseCase,
    private val addAnimalUseCase: AddAnimalUseCase,
    private val updateAnimalUseCase: UpdateAnimalUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnimalFormState())
    val uiState: StateFlow<AnimalFormState> = _uiState

    private val _lots = MutableStateFlow<List<Lot>>(emptyList())
    val lots: StateFlow<List<Lot>> = _lots

    private val _animalSaved = MutableStateFlow(false)
    val animalSaved: StateFlow<Boolean> = _animalSaved

    private val _error = MutableStateFlow<ErrorType?>(null)
    val error: StateFlow<ErrorType?> = _error

    fun dismissError() {
        _error.value = null
    }

    fun loadAnimal(animalId: String) {
        viewModelScope.launch {
            when (val animalResponse = getAnimalByIdUseCase(animalId)) {
                is Success -> {
                    val animal = animalResponse.data
                    _uiState.value = AnimalFormState(
                        id = animal.id,
                        tag = animal.tag,
                        nickname = animal.nickname,
                        lotId = animal.lotId,
                        animalGender = animal.animalGender,
                        animalBreed = animal.animalBreed,
                        birthDate = animal.birthDate,
                        purchaseValue = formatNumber(animal.purchaseValue.toString()),
                        purchaseDate = animal.purchaseDate,
                        saleValue = formatNumber(animal.saleValue.toString()),
                        saleDate = animal.saleDate,
                        animalState = animal.animalState
                    )
                }

                is Error -> _error.value = animalResponse.errorType
            }
        }
    }

    fun loadLots() {
        viewModelScope.launch {
            when (val lotsResponse = getAllLotsUseCase()) {
                is Success -> _lots.value = lotsResponse.data
                is Error -> _error.value = lotsResponse.errorType
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

    fun onGenderChange(animalGender: AnimalGender) {
        _uiState.value = _uiState.value.copy(animalGender = animalGender)
    }

    fun onBreedChange(animalBreed: AnimalBreed) {
        _uiState.value = _uiState.value.copy(animalBreed = animalBreed)
    }

    fun onBirthDateChange(birthDate: Instant) {
        _uiState.value = _uiState.value.copy(birthDate = birthDate)
    }

    fun onPurchaseValueChange(purchaseValue: String) {
        _uiState.value = _uiState.value.copy(purchaseValue = purchaseValue)
    }

    fun onPurchaseDateChange(purchaseDate: Instant) {
        _uiState.value = _uiState.value.copy(purchaseDate = purchaseDate)
    }

    fun onSaleValueChange(saleValue: String) {
        _uiState.value = _uiState.value.copy(saleValue = saleValue)
    }

    fun onSaleDateChange(saleDate: Instant) {
        _uiState.value = _uiState.value.copy(saleDate = saleDate)
    }

    fun onStateChange(animalState: AnimalState) {
        _uiState.value = _uiState.value.copy(animalState = animalState)
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
                animalGender = currentState.animalGender,
                animalBreed = currentState.animalBreed,
                birthDate = currentState.birthDate,
                purchaseValue = purchaseValue,
                purchaseDate = currentState.purchaseDate,
                saleValue = saleValue,
                saleDate = currentState.saleDate,
                animalState = currentState.animalState
            )

            if (animal.id == null) {
                val registerWeight = Weight(weight = weight, date = Instant.now())
                when (val animalResponse = addAnimalUseCase(animal, registerWeight)) {
                    is Success -> _animalSaved.value = true
                    is Error -> _error.value = animalResponse.errorType
                }
            } else {
                when (val animalResponse = updateAnimalUseCase(animal)) {
                    is Success -> _animalSaved.value = true
                    is Error -> _error.value = animalResponse.errorType
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
    val animalGender: AnimalGender = AnimalGender.Male,
    val animalBreed: AnimalBreed = AnimalBreed.ZEBU,
    val birthDate: Instant = Instant.now(),
    val purchaseValue: String = "",
    val purchaseDate: Instant = Instant.now(),
    val saleValue: String = "",
    val saleDate: Instant = Instant.now(),
    val animalState: AnimalState = AnimalState.Healthy,
    val weight: String = ""
)