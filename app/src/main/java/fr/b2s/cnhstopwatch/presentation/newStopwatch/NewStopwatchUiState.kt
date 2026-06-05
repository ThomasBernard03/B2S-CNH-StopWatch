package fr.b2s.cnhstopwatch.presentation.newStopwatch

import fr.b2s.cnhstopwatch.domain.models.Stopwatch

data class NewStopwatchUiState(
    val name: String = "",
    val isLoading: Boolean = false,
    val createdStopWatch: Stopwatch? = null
)
