package fr.b2s.cnhstopwatch.presentation.newStopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.b2s.cnhstopwatch.domain.usecases.CreateStopwatchUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewStopwatchViewModel(
    private val createStopwatchUseCase: CreateStopwatchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewStopwatchUiState())
    val uiState: StateFlow<NewStopwatchUiState> = _uiState.asStateFlow()

    fun onEvent(event: NewStopwatchEvent) {
        when (event) {
            is NewStopwatchEvent.OnNameChanged -> onNameChanged(event.name)
            is NewStopwatchEvent.OnCreateStopWatch -> createStopwatch()
            NewStopwatchEvent.OnGoBack, is NewStopwatchEvent.OnStopWatchCreated -> Unit
        }
    }

    private fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    private fun createStopwatch() {
        if (_uiState.value.name.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val createdStopwatch = createStopwatchUseCase(_uiState.value.name)
            _uiState.update { it.copy(name = "", createdStopWatch = createdStopwatch) }
        }
        .invokeOnCompletion {
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
