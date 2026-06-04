package fr.b2s.cnhstopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.b2s.cnhstopwatch.presentation.core.theme.CNHStopWatchTheme
import fr.b2s.cnhstopwatch.presentation.newStopwatch.NewStopwatchScreen
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CNHStopWatchTheme {
                NewStopwatchScreen(
                    onNavigateBack = {

                    },
                    viewModel = koinViewModel()
                )
            }
        }
    }
}