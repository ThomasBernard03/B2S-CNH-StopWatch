package fr.b2s.cnhstopwatch.presentation.stopwatchList

sealed interface StopwatchListEvent {
    data object OnCreateNew : StopwatchListEvent
    data class OnStopwatchClick(val id: Long) : StopwatchListEvent
}
