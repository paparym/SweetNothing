package com.prestigerito.sweetnothing.ui.menu

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.graphics.graphicsLayer
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
        modifier = Modifier.fillMaxSize(),
    ) {
        EndlessBackground(
            asset = MR.images.symbol_bg,
        )
        Row(
            modifier = Modifier.fillMaxWidth()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedItem(
                    modifier = Modifier.size(100.dp),
                    assets = coinAssets,
                    animationType = AnimationType.Y_AXIS_ROTATION
                )
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    modifier = Modifier.height(100.dp),
                    painter = painterResource(MR.images.grab),
                    contentDescription = null,
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedItem(
                    modifier = Modifier.size(100.dp),
                    assets = coinAssets,
                    animationType = AnimationType.Y_AXIS_ROTATION
                )
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    modifier = Modifier.height(100.dp),
                    painter = painterResource(MR.images.grab),
                    contentDescription = null,
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
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
                AnimatedItem(
                    modifier = Modifier.size(100.dp),
                    assets = mainHeroAssets,
                )
            }
        }
    }
}

@Composable
fun AnimatedItems() {

}

@Composable
fun AnimatedItem(
    modifier: Modifier = Modifier,
    assets: List<ImageResource>,
    animationType: AnimationType = AnimationType.NO_ANIMATION,
) {
    var currentResource by remember {
        mutableStateOf(
            assets.first()
        )
    }
    val yAxisMovement = remember { Animatable(0f) }
    LaunchedEffect(animationType) {
        when (animationType) {
            AnimationType.Y_AXIS_ROTATION -> {
                while (true) {
                    yAxisMovement.animateTo(
                        targetValue = 60f,
                        animationSpec = tween(easing = LinearEasing)
                    )
                    yAxisMovement.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(easing = LinearEasing)
                    )
                    yAxisMovement.animateTo(
                        targetValue = -60f,
                        animationSpec = tween(easing = LinearEasing)
                    )
                }
            }

            AnimationType.ASSET_CHANGE -> {
                while (true) {
                    assets.forEach {
                        currentResource = it
                        delay(100)
                    }
                }
            }

            AnimationType.NO_ANIMATION -> {}
        }

    }
    Image(
        modifier = modifier.graphicsLayer {
            this.rotationY = yAxisMovement.value
        },
        painter = painterResource(currentResource),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
    )
}

enum class AnimationType {
    Y_AXIS_ROTATION, NO_ANIMATION, ASSET_CHANGE
}

@Composable
fun EndlessBackground(
    asset: ImageResource,
) {
    val offset = remember { Animatable(0f) }
    val offset2 = remember { Animatable(0f) }
    val animation = tween<Float>(durationMillis = 10000, easing = LinearEasing)
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        LaunchedEffect(this.constraints.maxHeight) {
            launch {
                offset.animateTo(
                    targetValue = constraints.maxHeight.toFloat(),
                    animationSpec = infiniteRepeatable(
                        animation = animation,
                    ),
                )
            }
            launch {
                offset2.animateTo(
                    targetValue = constraints.maxHeight.toFloat(),
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
                        y = -(constraints.maxHeight.toFloat().toInt()) + (offset2.value.toInt()),
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
}
