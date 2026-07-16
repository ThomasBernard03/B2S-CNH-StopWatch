package fr.b2s.cnhstopwatch.presentation.newStopwatches

sealed interface NewStopwatchesEvent {
    data class OnStopwatchNameChanged(val index: Int, val name: String) : NewStopwatchesEvent
    data object OnAddStopwatchName : NewStopwatchesEvent
    data object OnCreateStopwatches : NewStopwatchesEvent
    data object OnGoBack : NewStopwatchesEvent
    data object OnStopwatchesCreated : NewStopwatchesEvent
}
