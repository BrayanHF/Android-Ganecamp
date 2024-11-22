package com.ganecamp.ui.screen.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.enums.EntityType.Animal
import com.ganecamp.domain.enums.ErrorType
import com.ganecamp.domain.model.EntityEvent
import com.ganecamp.domain.model.Event
import com.ganecamp.domain.result.OperationResult.Error
import com.ganecamp.domain.result.OperationResult.Success
import com.ganecamp.domain.usecase.event.AddFarmEventUseCase
import com.ganecamp.domain.usecase.event.GetAllFarmEventsUseCase
import com.ganecamp.domain.usecase.eventapplied.AddEntityEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class EventAddFormViewModel @Inject constructor(
    private val getAllFarmEventsUseCase: GetAllFarmEventsUseCase,
    private val addFarmEventUseCase: AddFarmEventUseCase,
    private val addEntityEventUseCase: AddEntityEventUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventFormState())
    val uiState: StateFlow<EventFormState> = _uiState

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _eventSaved = MutableStateFlow(false)
    val eventSaved: StateFlow<Boolean> = _eventSaved

    private val _error = MutableStateFlow<ErrorType?>(null)
    val error: StateFlow<ErrorType?> = _error

    fun dismissError() {
        _error.value = null
    }

    fun loadEvents() {
        viewModelScope.launch {
            when (val result = getAllFarmEventsUseCase()) {
                is Success -> _events.value = result.data
                is Error -> _error.value = result.errorType
            }
        }
    }

    fun onEventSelected(eventId: String, eventTitle: String) {
        _uiState.value = _uiState.value.copy(eventId = eventId, title = eventTitle)
    }

    fun onTitleChange(name: String) {
        _uiState.value = _uiState.value.copy(title = name)
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

    fun saveEvent() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState.isNew) {
                val newEvent =
                    Event(title = currentState.title, description = currentState.description)
                when (val result = addFarmEventUseCase(newEvent)) {
                    is Success -> currentState.eventId = result.data
                    is Error -> {
                        _error.value = result.errorType
                        return@launch
                    }
                }
            }

            val newEntityEvent = EntityEvent(
                eventId = currentState.eventId, date = currentState.date
            )
            when (val result = addEntityEventUseCase.invoke(
                entityId = currentState.entityId,
                entityEvent = newEntityEvent,
                entityType = currentState.entityType
            )) {
                is Success -> _eventSaved.value = true
                is Error -> _error.value = result.errorType
            }
        }
    }

    fun loadEntityType(entityId: String, entityType: EntityType) {
        _uiState.value = _uiState.value.copy(entityId = entityId, entityType = entityType)
    }

}

data class EventFormState(
    var eventId: String = "",
    val title: String = "",
    val description: String = "",
    val date: Instant = Instant.now(),
    val entityId: String = "",
    val isNew: Boolean = true,
    val entityType: EntityType = Animal
)