package fr.b2s.cnhstopwatch.presentation.stopwatchDetail

import fr.b2s.cnhstopwatch.domain.models.Stopwatch

data class StopwatchDetailUiState(
    val stopwatch: Stopwatch? = null,
    val isLoading: Boolean = true,
    val displayedMillis: Long = 0L
)
