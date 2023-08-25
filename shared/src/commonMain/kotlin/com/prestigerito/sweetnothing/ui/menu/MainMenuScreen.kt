package com.prestigerito.sweetnothing.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.prestigerito.sweetnothing.presentation.MainMenuViewModel

@Composable
fun MainMenu(
    text: String,
    viewModel: MainMenuViewModel,
    onGoNextClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.error),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            Button(onClick = onGoNextClick) {
                Text(text = text)
            }
            Button(onClick = { viewModel.addScoreToDb() }) {
                Text(text = "Add Score 1-100")
            }
            LazyColumn {
                items(state.scores) {
                    Text("item: $it")
                }
            }
        }
    }
}
