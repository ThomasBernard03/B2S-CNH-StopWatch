package fr.b2s.cnhstopwatch.presentation.stopwatchList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.b2s.cnhstopwatch.domain.models.Stopwatch
import fr.b2s.cnhstopwatch.presentation.core.theme.CNHStopWatchTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopwatchListScreen(
    uiState: StopwatchListUiState,
    onEvent: (StopwatchListEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stopwatches") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(StopwatchListEvent.OnCreateNew) }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "New Stopwatch"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.stopwatches.isEmpty()) {
                Text(
                    text = "No stopwatches yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.stopwatches, key = { it.id }) { stopwatch ->
                        StopwatchListItem(
                            stopwatch = stopwatch,
                            displayedMillis = uiState.displayedMillis[stopwatch.id] ?: 0L,
                            onClick = { onEvent(StopwatchListEvent.OnStopwatchClick(stopwatch.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StopwatchListItem(
    stopwatch: Stopwatch,
    displayedMillis: Long,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stopwatch.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = formatElapsedTime(displayedMillis),
                style = MaterialTheme.typography.headlineSmall,
                color = if (stopwatch.isRunning) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

private fun formatElapsedTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

@Preview(showBackground = true)
@Composable
private fun StopwatchListScreenPreview() {
    CNHStopWatchTheme {
        StopwatchListScreen(
            uiState = StopwatchListUiState(
                stopwatches = listOf(
                    Stopwatch(id = 1, name = "Task 1", createdAt = System.currentTimeMillis(), isRunning = true, accumulatedMillis = 60000L),
                    Stopwatch(id = 2, name = "Task 2", createdAt = System.currentTimeMillis(), accumulatedMillis = 125000L)
                ),
                isLoading = false,
                displayedMillis = mapOf(1L to 62000L, 2L to 125000L)
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StopwatchListScreenEmptyPreview() {
    CNHStopWatchTheme {
        StopwatchListScreen(
            uiState = StopwatchListUiState(isLoading = false),
            onEvent = {}
        )
    }
}
