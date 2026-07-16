package fr.b2s.cnhstopwatch.presentation.stopwatchList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.b2s.cnhstopwatch.domain.models.Stopwatch
import fr.b2s.cnhstopwatch.domain.usecases.GetAllStopwatchesUseCase
import fr.b2s.cnhstopwatch.domain.usecases.StartStopwatchUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class StopwatchListViewModel(
    private val getAllStopwatchesUseCase: GetAllStopwatchesUseCase,
    private val startStopwatchUseCase: StartStopwatchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StopwatchListUiState())
    val uiState: StateFlow<StopwatchListUiState> = _uiState.asStateFlow()

    private var currentStopwatches: List<Stopwatch> = emptyList()
    private var tickerJob: Job? = null

    init {
        viewModelScope.launch {
            getAllStopwatchesUseCase().collect { stopwatches ->
                currentStopwatches = stopwatches
                _uiState.update {
                    it.copy(
                        stopwatches = stopwatches,
                        isLoading = false,
                        displayedMillis = computeDisplayedMillis(stopwatches)
                    )
                }
                if (stopwatches.any { sw -> sw.isRunning }) {
                    startTicker()
                } else {
                    stopTicker()
                }
            }
        }
    }

    private fun computeDisplayedMillis(stopwatches: List<Stopwatch>): Map<Long, Long> {
        val now = System.currentTimeMillis()
        return stopwatches.associate { sw ->
            sw.id to if (sw.isRunning) {
                sw.accumulatedMillis + (now - sw.startedAt)
            } else {
                sw.accumulatedMillis
            }
        }
    }

    private fun startTicker() {
        stopTicker()
        tickerJob = viewModelScope.launch {
            while (isActive) {
                _uiState.update {
                    it.copy(displayedMillis = computeDisplayedMillis(currentStopwatches))
                }
                delay(200)
            }
        }
    }

    private fun stopTicker() {
        tickerJob?.cancel()
        tickerJob = null
    }

    fun onEvent(event: StopwatchListEvent) {
        when (event) {
            StopwatchListEvent.OnCreateMultiple -> Unit
            is StopwatchListEvent.OnStopwatchClick -> Unit
            is StopwatchListEvent.OnStartStopwatches -> startStopwatches(event.ids)
        }
    }

    private fun startStopwatches(ids: Set<Long>) {
        viewModelScope.launch {
            currentStopwatches
                .filter { stopwatch -> stopwatch.id in ids && !stopwatch.isRunning }
                .forEach { stopwatch -> startStopwatchUseCase(stopwatch.id) }
        }
    }
}
