package com.ganecamp.ui.screen.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ganecamp.data.firibase.model.EntityEvent
import com.ganecamp.data.firibase.model.Event
import com.ganecamp.domain.services.EventAppliedService
import com.ganecamp.domain.services.EventService
import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.enums.EntityType.Animal
import com.ganecamp.domain.enums.RepositoryRespond
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class EventAddFormViewModel @Inject constructor(
    private val eventService: EventService, private val eventAppliedService: EventAppliedService
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventFormState())
    val uiState: StateFlow<EventFormState> = _uiState

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _eventSaved = MutableStateFlow(false)
    val eventSaved: StateFlow<Boolean> = _eventSaved

    private val _error = MutableStateFlow(RepositoryRespond.OK)
    val error: StateFlow<RepositoryRespond> = _error

    fun loadEvents() {
        viewModelScope.launch {
            val eventResponse = eventService.getAllEvents()
            if (eventResponse.second == RepositoryRespond.OK) {
                _events.value = eventResponse.first
            } else {
                _error.value = eventResponse.second
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
                val eventResponse = eventService.createEvent(
                    Event(
                        title = currentState.title, description = currentState.description
                    )
                )
                if (eventResponse.second == RepositoryRespond.OK) {
                    currentState.eventId = eventResponse.first!!
                } else {
                    _error.value = eventResponse.second
                    return@launch
                }
            }
            val applyEventResponse = eventAppliedService.createEntityEvent(
                entityId = currentState.entityId, entityEvent = EntityEvent(
                    eventId = currentState.eventId, date = Timestamp(currentState.date)
                ), entityType = currentState.entityType
            )

            if (applyEventResponse == RepositoryRespond.OK) {
                _eventSaved.value = true
            } else {
                _error.value = applyEventResponse
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