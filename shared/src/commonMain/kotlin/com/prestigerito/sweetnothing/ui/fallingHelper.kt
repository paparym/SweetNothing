package com.prestigerito.sweetnothing.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Stable
class LevelInfoState {
    var score by mutableStateOf(0)
    fun updateScore() {
        this.score++
    }
}

@Composable
fun rememberLevelInfoState() = rememberSaveable(
    saver = listSaver<LevelInfoState, Any>(
        save = { levelInfoState ->
            listOf(
                levelInfoState.score,
            )
        },
        restore = { levelInfoList ->
            LevelInfoState().apply {
                score = levelInfoList[0] as Int
            }
        },
    ),
) {
    LevelInfoState()
}
