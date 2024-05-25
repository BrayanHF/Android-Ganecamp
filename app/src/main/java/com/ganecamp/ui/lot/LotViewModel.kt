package com.ganecamp.ui.lot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.model.Lot
import com.ganecamp.domain.services.LotService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LotViewModel @Inject constructor(private val lotService: LotService) : ViewModel() {

    private val _lots = MutableLiveData<List<Lot>>()
    val lots: LiveData<List<Lot>> = _lots

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadLots()
    }

    private fun loadLots() {
        viewModelScope.launch {
            _lots.value = lotService.getAllLots()
            _isLoading.value = false
        }
    }

}