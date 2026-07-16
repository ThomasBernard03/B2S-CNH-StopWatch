package fr.b2s.cnhstopwatch.presentation.newStopwatches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.b2s.cnhstopwatch.domain.usecases.CreateStopwatchUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewStopwatchesViewModel(
    private val createStopwatchUseCase: CreateStopwatchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewStopwatchesUiState())
    val uiState: StateFlow<NewStopwatchesUiState> = _uiState.asStateFlow()

    fun onEvent(event: NewStopwatchesEvent) {
        when (event) {
            is NewStopwatchesEvent.OnStopwatchNameChanged -> onStopwatchNameChanged(event.index, event.name)
            NewStopwatchesEvent.OnAddStopwatchName -> addStopwatchName()
            NewStopwatchesEvent.OnCreateStopwatches -> createStopwatches()
            NewStopwatchesEvent.OnGoBack, NewStopwatchesEvent.OnStopwatchesCreated -> Unit
        }
    }

    private fun onStopwatchNameChanged(index: Int, name: String) {
        _uiState.update { state ->
            if (index !in state.stopwatchNames.indices) return@update state
            state.copy(
                stopwatchNames = state.stopwatchNames.mapIndexed { nameIndex, currentName ->
                    if (nameIndex == index) name else currentName
                }
            )
        }
    }

    private fun addStopwatchName() {
        _uiState.update { state ->
            state.copy(stopwatchNames = state.stopwatchNames + "")
        }
    }

    private fun createStopwatches() {
        val stopwatchNames = _uiState.value.stopwatchNames
            .map { it.trim() }
            .filter { it.isNotBlank() }
        if (stopwatchNames.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            stopwatchNames.forEach { name -> createStopwatchUseCase(name) }
            _uiState.update {
                it.copy(
                    stopwatchNames = listOf(""),
                    areStopwatchesCreated = true
                )
            }
        }
            .invokeOnCompletion {
                _uiState.update { it.copy(isLoading = false) }
            }
    }
}
