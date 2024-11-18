package com.ganecamp.ui.screen.lot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.services.LotService
import com.ganecamp.data.firibase.model.Lot
import com.ganecamp.domain.enums.RepositoryRespond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LotViewModel @Inject constructor(private val lotService: LotService) : ViewModel() {

    private val _lots = MutableStateFlow<List<Lot>>(emptyList())
    val lots: StateFlow<List<Lot>> = _lots

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow(RepositoryRespond.OK)
    val error: StateFlow<RepositoryRespond> = _error

    fun loadLots() {
        viewModelScope.launch {
            val lotRespond = lotService.getAllLots()
            if (lotRespond.second == RepositoryRespond.OK) {
                _lots.value = lotRespond.first
            } else {
                _error.value = lotRespond.second
            }
            _isLoading.value = false
        }
    }

}