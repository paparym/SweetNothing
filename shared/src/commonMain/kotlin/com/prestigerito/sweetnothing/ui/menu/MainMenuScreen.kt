package com.prestigerito.sweetnothing.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.prestigerito.sweetnothing.MR
import com.prestigerito.sweetnothing.presentation.MainMenuViewModel
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.delay

@Composable
fun MainMenu(
    viewModel: MainMenuViewModel,
    onPlayClicked: () -> Unit,
    onSelectLevelClicked: () -> Unit,
    onHighScoresClicked: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.error),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(top = 150.dp),
            text = stringResource(MR.strings.sweetNothing),
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = onPlayClicked) {
                Text(text = stringResource(MR.strings.play))
            }
            Button(onClick = onSelectLevelClicked) {
                Text(text = stringResource(MR.strings.selectLevel))
            }
            Button(onClick = onHighScoresClicked) {
                Text(text = stringResource(MR.strings.highScores))
            }
            Image(
                modifier = Modifier.height(100.dp),
                painter = painterResource(MR.images.shop),
                contentDescription = null,
            )
            AnimatedHero(
                modifier = Modifier.size(100.dp),
                assets = listOf(
                    MR.images.coin1,
                    MR.images.coin2,
                    MR.images.coin3,
                    MR.images.coin4,
                    MR.images.coin5,
                    MR.images.coin6,
                    MR.images.coin7,
                    MR.images.coin8,
                    MR.images.coin9,
                    MR.images.coin10,
                    MR.images.coin11,
                    MR.images.coin12,
                    MR.images.coin13,
                    MR.images.coin14,
                ),
            )
            AnimatedHero(
                modifier = Modifier.size(100.dp),
                assets = listOf(
                    MR.images.walk_right_0,
                    MR.images.walk_right_1,
                    MR.images.walk_right_2,
                    MR.images.walk_right_3,
                ),
            )
        }
    }
}

@Composable
fun AnimatedHero(
    modifier: Modifier = Modifier,
    assets: List<ImageResource>,
) {
    var currentResource by remember { mutableStateOf(assets.first()) }
    var index by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            if (assets.size == index) index = 0
            currentResource = assets[index]
            index++
            delay(100)
        }
    }
    Image(
        modifier = modifier,
        painter = painterResource(currentResource),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
    )
}
