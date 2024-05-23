package com.gclewis

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gclewis.ui.theme.ViewModelTestTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViewModelTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    GameScreen()
                }
            }
        }
    }

    @Composable
    fun GameScreen(
        viewModel: GameViewModel = viewModel()
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle(
            androidx.compose.ui.platform.LocalLifecycleOwner.current
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row() {
                Text(text = "${uiState.homeTeamScore}")
            }
            Row() {
                Button(onClick = {
                    viewModel.homeTeamScores()
                }) {
                    Text(text = "Home Team Scores")
                }
            }
            Row() {
                Text(text = "${uiState.awayTeamScore}")
            }
            Row() {
                Button(onClick = {
                    viewModel.awayTeamScores()
                }) {
                    Text(text = "Away Team Scores")
                }
            }
            Row() {
                Button(onClick = {
                    viewModel.reset()
                }) {
                    Text(text = "Reset")
                }
            }
        }
    }
}

data class GameUIState(
    val homeTeamScore: Int? = 0,
    val awayTeamScore: Int? = 0,
)

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUIState())
    val uiState: StateFlow<GameUIState> = _uiState.asStateFlow()

    fun homeTeamScores() {
        _uiState.update { currentState ->
            currentState.copy(
                homeTeamScore = currentState.homeTeamScore?.plus(1),
            )
        }
    }

    fun awayTeamScores() {
        _uiState.update { currentState ->
            currentState.copy(
                awayTeamScore = currentState.awayTeamScore?.plus(1),
            )
        }
    }

    fun reset() {
        _uiState.update { currentState ->
            currentState.copy(
                homeTeamScore = 0,
                awayTeamScore = 0,
            )
        }
    }
}
