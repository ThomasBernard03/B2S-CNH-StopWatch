package fr.b2s.cnhstopwatch.presentation.newStopwatches

data class NewStopwatchesUiState(
    val stopwatchNames: List<String> = listOf(""),
    val isLoading: Boolean = false,
    val areStopwatchesCreated: Boolean = false
)
