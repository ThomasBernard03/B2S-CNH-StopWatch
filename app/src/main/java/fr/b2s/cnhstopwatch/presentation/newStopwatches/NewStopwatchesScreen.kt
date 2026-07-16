package fr.b2s.cnhstopwatch.presentation.newStopwatches

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.b2s.cnhstopwatch.presentation.core.theme.CNHStopWatchTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewStopwatchesScreen(
    uiState: NewStopwatchesUiState,
    onEvent: (NewStopwatchesEvent) -> Unit
) {
    LaunchedEffect(uiState.areStopwatchesCreated) {
        if (uiState.areStopwatchesCreated) {
            onEvent(NewStopwatchesEvent.OnStopwatchesCreated)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nouveaux chronomètres") },
                navigationIcon = {
                    IconButton(onClick = { onEvent(NewStopwatchesEvent.OnGoBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
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
            uiState.stopwatchNames.forEachIndexed { index, name ->
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        onEvent(NewStopwatchesEvent.OnStopwatchNameChanged(index, it))
                    },
                    label = { Text("Nom du chronomètre ${index + 1}") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            IconButton(
                onClick = { onEvent(NewStopwatchesEvent.OnAddStopwatchName) },
                enabled = !uiState.isLoading
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Ajouter un chronomètre"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onEvent(NewStopwatchesEvent.OnCreateStopwatches) },
                enabled = uiState.stopwatchNames.any { it.isNotBlank() } && !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Créer les chronomètres")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewStopwatchesScreenPreview() {
    CNHStopWatchTheme {
        NewStopwatchesScreen(
            uiState = NewStopwatchesUiState(stopwatchNames = listOf("Chrono 1", "Chrono 2")),
            onEvent = {}
        )
    }
}
