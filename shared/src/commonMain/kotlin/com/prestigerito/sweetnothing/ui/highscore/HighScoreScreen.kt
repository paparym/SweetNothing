package com.prestigerito.sweetnothing.ui.highscore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.prestigerito.sweetnothing.presentation.HighScoreViewModel

@Composable
fun HighScoreScreen(
    viewModel: HighScoreViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Column {
            Text("High Score: ${state.score?.highScore}")
            Text("at level: ${state.score?.levelIndex}")
        }
    }
}
