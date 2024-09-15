package com.ganecamp.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.services.AnimalService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val animalService: AnimalService) : ViewModel() {

    private val _showErrorDialog = MutableLiveData(false)
    val showErrorDialog: LiveData<Boolean> = _showErrorDialog

    private val _showConfirmDialog = MutableLiveData(false)
    val showConfirmDialog: LiveData<Boolean> = _showConfirmDialog

    private val _animalId = MutableLiveData<Int>()
    val animalId: LiveData<Int> = _animalId

    fun onTagReceived(tag: String) {
        viewModelScope.launch {
            val id = try {
                animalService.getIdByTag(tag)
            } catch (e: Exception) {
                -1
            }
            _animalId.value = id

            when (id) {
                -1 -> _showErrorDialog.value = true
                0 -> _showConfirmDialog.value = true
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
        _animalId.value = 0
    }
}