package fr.b2s.cnhstopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.b2s.cnhstopwatch.presentation.core.theme.CNHStopWatchTheme
import fr.b2s.cnhstopwatch.presentation.newStopwatch.NewStopwatchEvent
import fr.b2s.cnhstopwatch.presentation.newStopwatch.NewStopwatchScreen
import fr.b2s.cnhstopwatch.presentation.newStopwatch.NewStopwatchViewModel
import fr.b2s.cnhstopwatch.presentation.stopwatchList.StopwatchListEvent
import fr.b2s.cnhstopwatch.presentation.stopwatchList.StopwatchListScreen
import fr.b2s.cnhstopwatch.presentation.stopwatchList.StopwatchListViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CNHStopWatchTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "stopwatch_list"
                ) {
                    composable("stopwatch_list") {
                        val viewModel = koinViewModel<StopwatchListViewModel>()
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                        StopwatchListScreen(
                            uiState = uiState,
                            onEvent = { event ->
                                when (event) {
                                    StopwatchListEvent.OnCreateNew -> navController.navigate("new_stopwatch")
                                    is StopwatchListEvent.OnStopwatchClick -> Unit
                                }
                                viewModel.onEvent(event)
                            }
                        )
                    }
                    composable("new_stopwatch") {
                        val viewModel = koinViewModel<NewStopwatchViewModel>()
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                        NewStopwatchScreen(
                            uiState = uiState,
                            onEvent = { event ->
                                when (event) {
                                    NewStopwatchEvent.OnGoBack -> navController.popBackStack()
                                    else -> viewModel.onEvent(event)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}