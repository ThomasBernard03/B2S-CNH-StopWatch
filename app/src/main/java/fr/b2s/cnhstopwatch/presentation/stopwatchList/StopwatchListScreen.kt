package fr.b2s.cnhstopwatch.presentation.stopwatchList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Chronomètres", "Classement")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chronomètres") }
            )
        },
        floatingActionButton = {
            if (selectedTabIndex == 0) {
                FloatingActionButton(onClick = { onEvent(StopwatchListEvent.OnCreateNew) }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Nouveau chronomètre"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else if (selectedTabIndex == 0) {
                StopwatchListContent(uiState = uiState, onEvent = onEvent)
            } else {
                StopwatchRankingContent(uiState = uiState, onEvent = onEvent)
            }
        }
    }
}

@Composable
private fun StopwatchListContent(
    uiState: StopwatchListUiState,
    onEvent: (StopwatchListEvent) -> Unit
) {
    if (uiState.stopwatches.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Aucun chronomètre",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
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

@Composable
private fun StopwatchRankingContent(
    uiState: StopwatchListUiState,
    onEvent: (StopwatchListEvent) -> Unit
) {
    val rankedStopwatches = uiState.stopwatches
        .filter { !it.isRunning && it.accumulatedMillis > 0L }
        .sortedBy { it.accumulatedMillis }

    if (rankedStopwatches.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Aucun chronomètre terminé",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(rankedStopwatches, key = { _, stopwatch -> stopwatch.id }) { index, stopwatch ->
                StopwatchRankingItem(
                    rank = index + 1,
                    stopwatch = stopwatch,
                    onClick = { onEvent(StopwatchListEvent.OnStopwatchClick(stopwatch.id)) }
                )
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

@Composable
private fun StopwatchRankingItem(
    rank: Int,
    stopwatch: Stopwatch,
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
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#$rank",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stopwatch.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = formatElapsedTime(stopwatch.accumulatedMillis),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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

@Preview(showBackground = true)
@Composable
private fun StopwatchListContentPreview() {
    CNHStopWatchTheme {
        StopwatchListContent(
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
private fun StopwatchRankingContentPreview() {
    CNHStopWatchTheme {
        StopwatchRankingContent(
            uiState = StopwatchListUiState(
                stopwatches = listOf(
                    Stopwatch(id = 1, name = "Task 1", createdAt = System.currentTimeMillis(), accumulatedMillis = 60000L),
                    Stopwatch(id = 2, name = "Task 2", createdAt = System.currentTimeMillis(), accumulatedMillis = 125000L)
                ),
                isLoading = false
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StopwatchListItemPreview() {
    CNHStopWatchTheme {
        StopwatchListItem(
            stopwatch = Stopwatch(id = 1, name = "Task 1", createdAt = System.currentTimeMillis()),
            displayedMillis = 62000L,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StopwatchRankingItemPreview() {
    CNHStopWatchTheme {
        StopwatchRankingItem(
            rank = 1,
            stopwatch = Stopwatch(
                id = 1,
                name = "Best Task",
                createdAt = System.currentTimeMillis(),
                accumulatedMillis = 62000L
            ),
            onClick = {}
        )
    }
}
