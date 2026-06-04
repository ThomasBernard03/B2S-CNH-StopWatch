package fr.b2s.cnhstopwatch.presentation.stopwatchList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.b2s.cnhstopwatch.domain.usecases.GetAllStopwatchesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StopwatchListViewModel(
    private val getAllStopwatchesUseCase: GetAllStopwatchesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StopwatchListUiState())
    val uiState: StateFlow<StopwatchListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllStopwatchesUseCase().collect { stopwatches ->
                _uiState.update { it.copy(stopwatches = stopwatches, isLoading = false) }
            }
        }
    }

    fun onEvent(event: StopwatchListEvent) {
        when (event) {
            StopwatchListEvent.OnCreateNew -> Unit
            is StopwatchListEvent.OnStopwatchClick -> Unit
        }
    }
}
