package fr.b2s.cnhstopwatch.presentation.stopwatchList

sealed interface StopwatchListEvent {
    data object OnCreateMultiple : StopwatchListEvent
    data class OnStopwatchClick(val id: Long) : StopwatchListEvent
}
