package com.ganecamp.ui.screen.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.services.AnimalService
import com.ganecamp.domain.enums.RepositoryRespond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val animalService: AnimalService) : ViewModel() {

    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog

    private val _showConfirmDialog = MutableStateFlow(false)
    val showConfirmDialog: StateFlow<Boolean> = _showConfirmDialog

    private val _animalId = MutableStateFlow<String?>(null)
    val animalId: StateFlow<String?> = _animalId

    private val _error = MutableStateFlow(RepositoryRespond.OK)
    val error: StateFlow<RepositoryRespond> = _error

    fun onTagReceived(tag: String) {
        viewModelScope.launch {
            val animalRespond = animalService.getAnimalByTag(tag)
            when (animalRespond.second) {
                RepositoryRespond.OK -> {
                    animalRespond.first?.let {
                        _animalId.value = it.id
                    }
                }

                RepositoryRespond.NOT_FOUND -> {
                    _showConfirmDialog.value = true
                }

                else -> {
                    _error.value = animalRespond.second
                    _showErrorDialog.value = true
                }
            }
        }
    }

    fun closeErrorDialog() {
        _showErrorDialog.value = false
    }

    fun closeConfirmDialog() {
        _showConfirmDialog.value = false
    }

    fun resetAnimalId() {
        _animalId.value = null
    }
}