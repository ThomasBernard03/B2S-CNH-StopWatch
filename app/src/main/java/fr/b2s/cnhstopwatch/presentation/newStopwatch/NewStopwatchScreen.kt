package fr.b2s.cnhstopwatch.presentation.newStopwatch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.b2s.cnhstopwatch.presentation.core.theme.CNHStopWatchTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewStopwatchScreen(
    onNavigateBack: () -> Unit,
    viewModel: NewStopwatchViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    NewStopwatchScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNameChanged = { viewModel.onEvent(NewStopwatchEvent.OnNameChanged(it)) },
        onCreateStopWatch = { viewModel.onEvent(NewStopwatchEvent.OnCreateStopWatch) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewStopwatchScreenContent(
    uiState: NewStopwatchUiState,
    onNavigateBack: () -> Unit,
    onNameChanged: (String) -> Unit,
    onCreateStopWatch: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Stopwatch") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChanged,
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onCreateStopWatch,
                enabled = uiState.name.isNotBlank() && !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Create")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewStopwatchScreenPreview() {
    CNHStopWatchTheme {
        NewStopwatchScreenContent(
            uiState = NewStopwatchUiState(),
            onNavigateBack = {},
            onNameChanged = {},
            onCreateStopWatch = {}
        )
    }
}
