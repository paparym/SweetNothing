package com.prestigerito.sweetnothing.ui.game

import androidx.compose.runtime.Composable
import com.prestigerito.sweetnothing.MR
import com.prestigerito.sweetnothing.presentation.LevelState
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun LevelState.stringValue(): String {
    return when (this) {
        is LevelState.Level -> stringResource(MR.strings.level, index)
        is LevelState.NewRecord -> stringResource(MR.strings.newHighScore, highScore)
        LevelState.TryAgain -> stringResource(MR.strings.tryAgain)
    }
}