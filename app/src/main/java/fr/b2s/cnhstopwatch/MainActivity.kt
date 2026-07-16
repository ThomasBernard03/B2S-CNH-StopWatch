package fr.b2s.cnhstopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.b2s.cnhstopwatch.presentation.core.theme.CNHStopWatchTheme
import fr.b2s.cnhstopwatch.presentation.newStopwatches.NewStopwatchesEvent
import fr.b2s.cnhstopwatch.presentation.newStopwatches.NewStopwatchesScreen
import fr.b2s.cnhstopwatch.presentation.newStopwatches.NewStopwatchesViewModel
import fr.b2s.cnhstopwatch.presentation.stopwatchDetail.StopwatchDetailEvent
import fr.b2s.cnhstopwatch.presentation.stopwatchDetail.StopwatchDetailScreen
import fr.b2s.cnhstopwatch.presentation.stopwatchDetail.StopwatchDetailViewModel
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
                                StopwatchListEvent.OnCreateMultiple -> navController.navigate("new_stopwatches"){
                                    launchSingleTop = true
                                }
                                is StopwatchListEvent.OnStopwatchClick -> navController.navigate("stopwatch_detail/${event.id}"){
                                    launchSingleTop = true
                                }
                            }
                            viewModel.onEvent(event)
                        }
                        )
                    }
                    composable("new_stopwatches") {
                        val viewModel = koinViewModel<NewStopwatchesViewModel>()
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                        NewStopwatchesScreen(
                            uiState = uiState,
                            onEvent = { event ->
                                when (event) {
                                    NewStopwatchesEvent.OnGoBack -> navController.popBackStack()
                                    NewStopwatchesEvent.OnStopwatchesCreated -> navController.popBackStack()
                                    else -> viewModel.onEvent(event)
                                }
                            }
                        )
                    }
                    composable(
                        route = "stopwatch_detail/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) {
                        val viewModel = koinViewModel<StopwatchDetailViewModel>()
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                        LaunchedEffect(uiState.isDeleted) {
                            if (uiState.isDeleted) {
                                navController.navigateUp()
                            }
                        }
                        StopwatchDetailScreen(
                            uiState = uiState,
                            onEvent = { event ->
                                when (event) {
                                    StopwatchDetailEvent.OnGoBack -> navController.navigateUp()
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
