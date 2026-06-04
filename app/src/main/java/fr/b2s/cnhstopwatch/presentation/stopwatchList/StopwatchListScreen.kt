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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                text = formatDate(stopwatch.createdAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
private fun StopwatchListScreenPreview() {
    CNHStopWatchTheme {
        StopwatchListScreen(
            uiState = StopwatchListUiState(
                stopwatches = listOf(
                    Stopwatch(id = 1, name = "Task 1", createdAt = System.currentTimeMillis()),
                    Stopwatch(id = 2, name = "Task 2", createdAt = System.currentTimeMillis())
                ),
                isLoading = false
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
