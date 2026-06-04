package fr.b2s.cnhstopwatch.presentation.stopwatchList

import fr.b2s.cnhstopwatch.domain.models.Stopwatch

data class StopwatchListUiState(
    val stopwatches: List<Stopwatch> = emptyList(),
    val isLoading: Boolean = true
)
