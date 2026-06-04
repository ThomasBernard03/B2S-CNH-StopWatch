package fr.b2s.cnhstopwatch.presentation.stopwatchDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
fun StopwatchDetailScreen(
    uiState: StopwatchDetailUiState,
    onEvent: (StopwatchDetailEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.stopwatch?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = { onEvent(StopwatchDetailEvent.OnGoBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        text = formatElapsedTime(uiState.displayedMillis),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onEvent(StopwatchDetailEvent.OnReset) },
                            modifier = Modifier.weight(1f),
                            enabled = uiState.displayedMillis > 0L
                        ) {
                            Text("Reset")
                        }

                        val isRunning = uiState.stopwatch?.isRunning ?: false
                        Button(
                            onClick = {
                                if (isRunning) {
                                    onEvent(StopwatchDetailEvent.OnPause)
                                } else {
                                    onEvent(StopwatchDetailEvent.OnStart)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (isRunning) "Pause" else "Start")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

private fun formatElapsedTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    val centiseconds = (millis % 1000) / 10
    return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, centiseconds)
}

@Preview(showBackground = true)
@Composable
private fun StopwatchDetailScreenStoppedPreview() {
    CNHStopWatchTheme {
        StopwatchDetailScreen(
            uiState = StopwatchDetailUiState(
                stopwatch = Stopwatch(
                    id = 1,
                    name = "My Stopwatch",
                    createdAt = System.currentTimeMillis(),
                    isRunning = false,
                    accumulatedMillis = 125000L
                ),
                isLoading = false,
                displayedMillis = 125000L
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StopwatchDetailScreenRunningPreview() {
    CNHStopWatchTheme {
        StopwatchDetailScreen(
            uiState = StopwatchDetailUiState(
                stopwatch = Stopwatch(
                    id = 1,
                    name = "Running Stopwatch",
                    createdAt = System.currentTimeMillis(),
                    isRunning = true,
                    startedAt = System.currentTimeMillis(),
                    accumulatedMillis = 3600000L
                ),
                isLoading = false,
                displayedMillis = 3600123L
            ),
            onEvent = {}
        )
    }
}
