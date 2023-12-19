package com.prestigerito.sweetnothing.ui.highscore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prestigerito.sweetnothing.MR
import com.prestigerito.sweetnothing.presentation.highscores.HighScoreComponent
import com.prestigerito.sweetnothing.ui.menu.EndlessBackground
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun HighScoreScreen(component: HighScoreComponent) {
    val state by component.state.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        EndlessBackground(
            asset = MR.images.symbol_bg,
            topToBottom = false,
        )
        Icon(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(16.dp)
                .align(Alignment.TopStart)
                .clickable { component.goBack() },
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
        )
        AnimatedVisibility(
            visible = state.score != null,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                state.score?.highScore?.let {
                    Text(
                        text = stringResource(MR.strings.highestScore, it),
                        fontSize = 30.sp,
                    )
                }
                state.score?.levelIndex?.let {
                    Text(
                        text = stringResource(MR.strings.atLevel, it),
                        fontSize = 30.sp,
                    )
                }
            }
        }
    }
}
