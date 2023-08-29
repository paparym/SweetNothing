package com.prestigerito.sweetnothing.ui.menu

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.prestigerito.sweetnothing.MR
import com.prestigerito.sweetnothing.presentation.MainMenuViewModel
import com.prestigerito.sweetnothing.ui.game.coinAssets
import com.prestigerito.sweetnothing.ui.game.mainHeroAssets
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                assets = coinAssets,
            )
            AnimatedHero(
                modifier = Modifier.size(100.dp),
                assets = mainHeroAssets,
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

@Composable
fun EndlessBackground(
    asset: ImageResource,
    screenHeightPx: Float,
) {
    val offset = remember { Animatable(0f) }
    val offset2 = remember { Animatable(0f) }
    val animation = tween<Float>(durationMillis = 2000, easing = LinearEasing)
    LaunchedEffect(screenHeightPx) {
        launch {
            offset.animateTo(
                targetValue = screenHeightPx,
                animationSpec = infiniteRepeatable(
                    animation = animation,
                ),
            )
        }
        launch {
            offset2.animateTo(
                targetValue = screenHeightPx,
                animationSpec = infiniteRepeatable(
                    animation = animation,
                ),
            )
        }
    }
    Image(
        modifier = Modifier
            .fillMaxSize()
            .offset {
                IntOffset(
                    y = -(screenHeightPx.toInt()) + (offset2.value.toInt()),
                    x = 0,
                )
            },
        painter = painterResource(asset),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
    )
    Image(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(y = offset.value.toInt(), x = 0) },
        painter = painterResource(asset),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
    )
}
