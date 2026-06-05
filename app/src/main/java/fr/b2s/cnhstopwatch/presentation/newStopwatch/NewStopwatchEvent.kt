package fr.b2s.cnhstopwatch.presentation.newStopwatch

import fr.b2s.cnhstopwatch.domain.models.Stopwatch

sealed interface NewStopwatchEvent {
    data class OnNameChanged(val name : String) : NewStopwatchEvent
    data object OnCreateStopWatch : NewStopwatchEvent
    data object OnGoBack : NewStopwatchEvent
    data class OnStopWatchCreated(val stopwatch: Stopwatch) : NewStopwatchEvent
}