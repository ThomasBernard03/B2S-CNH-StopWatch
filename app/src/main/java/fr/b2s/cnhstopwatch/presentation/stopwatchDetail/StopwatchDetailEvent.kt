package fr.b2s.cnhstopwatch.presentation.stopwatchDetail

sealed interface StopwatchDetailEvent {
    data object OnGoBack : StopwatchDetailEvent
    data object OnStart : StopwatchDetailEvent
    data object OnPause : StopwatchDetailEvent
    data object OnReset : StopwatchDetailEvent
}
