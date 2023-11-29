package com.prestigerito.sweetnothing.ui.menu

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.sp
import com.prestigerito.sweetnothing.MR
import com.prestigerito.sweetnothing.presentation.MainMenuViewModel
import com.prestigerito.sweetnothing.ui.MenuButton
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
    onHighScoresClicked: () -> Unit,
    onRulesClicked: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        EndlessBackground(
            asset = MR.images.symbol_bg,
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier,
                    text = stringResource(MR.strings.sweetNothing),
                    fontSize = 30.sp,
                )
                AnimatedItem(
                    modifier = Modifier.size(100.dp),
                    assets = mainHeroAssets,
                    animationType = AnimationType.ASSET_CHANGE,
                )
                Spacer(modifier = Modifier.height(20.dp))
                MenuButton(
                    text = stringResource(MR.strings.play),
                    onClick = onPlayClicked,
                )
                Spacer(modifier = Modifier.height(10.dp))
                MenuButton(
                    text = stringResource(MR.strings.highScores),
                    onClick = onHighScoresClicked,
                    enabled = state.highScoreAvailable,
                )
                Spacer(modifier = Modifier.height(10.dp))
                MenuButton(
                    text = stringResource(MR.strings.rules),
                    onClick = onRulesClicked,
                )
            }
        }
    }
}

@Composable
fun AnimatedItem(
    modifier: Modifier = Modifier,
    assets: List<ImageResource>,
    animationType: AnimationType = AnimationType.NO_ANIMATION,
) {
    var currentResource by remember {
        mutableStateOf(
            assets.first(),
        )
    }
    val yAxisMovement = remember { Animatable(0f) }
    LaunchedEffect(animationType) {
        when (animationType) {
            AnimationType.Y_AXIS_ROTATION -> {
                while (true) {
                    yAxisMovement.animateTo(
                        targetValue = 35f,
                        animationSpec = tween(easing = LinearEasing, durationMillis = 4000),
                    )
                    yAxisMovement.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(easing = LinearEasing),
                    )
                    yAxisMovement.animateTo(
                        targetValue = -35f,
                        animationSpec = tween(easing = LinearEasing),
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
    topToBottom: Boolean = true,
) {
    val offset = remember { Animatable(0f) }
    val offset2 = remember { Animatable(0f) }
    val animation = tween<Float>(durationMillis = 10000, easing = LinearEasing)
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        val targetValue = if (topToBottom) {
            constraints.maxHeight.toFloat()
        } else {
            -constraints.maxHeight.toFloat()
        }
        LaunchedEffect(this.constraints.maxHeight) {
            launch {
                offset.animateTo(
                    targetValue = targetValue,
                    animationSpec = infiniteRepeatable(
                        animation = animation,
                    ),
                )
            }
            launch {
                offset2.animateTo(
                    targetValue = targetValue,
                    animationSpec = infiniteRepeatable(
                        animation = animation,
                    ),
                )
            }
        }
        val offsetY = if (topToBottom) {
            -(constraints.maxHeight.toFloat().toInt()) + (offset2.value.toInt())
        } else {
            (constraints.maxHeight.toFloat().toInt()) + (offset2.value.toInt())
        }
        Image(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(
                        y = offsetY,
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
