package com.ganecamp.ui.screen.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.enums.ErrorType
import com.ganecamp.domain.result.OperationResult.Error
import com.ganecamp.domain.result.OperationResult.Success
import com.ganecamp.domain.usecase.animal.GetAnimalByTagUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val getAnimalByTagUseCase: GetAnimalByTagUseCase
) : ViewModel() {

    private val _showConfirmDialog = MutableStateFlow(false)
    val showConfirmDialog: StateFlow<Boolean> = _showConfirmDialog

    private val _animalId = MutableStateFlow<String?>(null)
    val animalId: StateFlow<String?> = _animalId

    private val _error = MutableStateFlow<ErrorType?>(null)
    val error: StateFlow<ErrorType?> = _error

    fun dismissError() {
        _error.value = null
    }

    fun onTagReceived(tag: String) {
        viewModelScope.launch {
            when (val result = getAnimalByTagUseCase(tag)) {
                is Success -> _animalId.value = result.data.id
                is Error -> {
                    if (result.errorType == ErrorType.NOT_FOUND) {
                        _showConfirmDialog.value = true
                    } else {
                        _error.value = result.errorType
                    }
                }
            }
        }
    }

    fun closeConfirmDialog() {
        _showConfirmDialog.value = false
    }

    fun resetAnimalId() {
        _animalId.value = null
    }

}