package fr.b2s.cnhstopwatch.presentation.stopwatchDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.b2s.cnhstopwatch.domain.usecases.GetStopwatchByIdUseCase
import fr.b2s.cnhstopwatch.domain.usecases.ResetStopwatchUseCase
import fr.b2s.cnhstopwatch.domain.usecases.StartStopwatchUseCase
import fr.b2s.cnhstopwatch.domain.usecases.StopStopwatchUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class StopwatchDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getStopwatchByIdUseCase: GetStopwatchByIdUseCase,
    private val startStopwatchUseCase: StartStopwatchUseCase,
    private val stopStopwatchUseCase: StopStopwatchUseCase,
    private val resetStopwatchUseCase: ResetStopwatchUseCase
) : ViewModel() {

    private val stopwatchId: Long = savedStateHandle.get<Long>("id") ?: 0L

    private val _uiState = MutableStateFlow(StopwatchDetailUiState())
    val uiState: StateFlow<StopwatchDetailUiState> = _uiState.asStateFlow()

    private var tickerJob: Job? = null

    init {
        viewModelScope.launch {
            getStopwatchByIdUseCase(stopwatchId).collect { stopwatch ->
                if (stopwatch != null) {
                    _uiState.update {
                        it.copy(
                            stopwatch = stopwatch,
                            isLoading = false,
                            displayedMillis = if (stopwatch.isRunning) {
                                stopwatch.accumulatedMillis + (System.currentTimeMillis() - stopwatch.startedAt)
                            } else {
                                stopwatch.accumulatedMillis
                            }
                        )
                    }
                    if (stopwatch.isRunning) {
                        startTicker(stopwatch.accumulatedMillis, stopwatch.startedAt)
                    } else {
                        stopTicker()
                    }
                }
            }
        }
    }

    private fun startTicker(accumulatedMillis: Long, startedAt: Long) {
        stopTicker()
        tickerJob = viewModelScope.launch {
            while (isActive) {
                _uiState.update {
                    it.copy(displayedMillis = accumulatedMillis + (System.currentTimeMillis() - startedAt))
                }
                delay(16)
            }
        }
    }

    private fun stopTicker() {
        tickerJob?.cancel()
        tickerJob = null
    }

    fun onEvent(event: StopwatchDetailEvent) {
        when (event) {
            StopwatchDetailEvent.OnGoBack -> Unit
            StopwatchDetailEvent.OnStart -> start()
            StopwatchDetailEvent.OnPause -> pause()
            StopwatchDetailEvent.OnReset -> reset()
        }
    }

    private fun start() {
        viewModelScope.launch { startStopwatchUseCase(stopwatchId) }
    }

    private fun pause() {
        viewModelScope.launch { stopStopwatchUseCase(stopwatchId) }
    }

    private fun reset() {
        viewModelScope.launch { resetStopwatchUseCase(stopwatchId) }
    }
}
