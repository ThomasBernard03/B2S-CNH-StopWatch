package fr.b2s.cnhstopwatch.presentation.newStopwatch

sealed interface NewStopwatchEvent {
    data class OnNameChanged(val name : String) : NewStopwatchEvent
    data object OnCreateStopWatch : NewStopwatchEvent
    data object OnGoBack : NewStopwatchEvent
}