package com.prestigerito.sweetnothing.ui.game

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.prestigerito.sweetnothing.MR
import com.prestigerito.sweetnothing.ui.menu.AnimatedHero
import com.prestigerito.sweetnothing.ui.menu.EndlessBackground
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun GameScreen() {
    var screenHeightPx by remember { mutableStateOf(0f) }
    var screenWidthPx by remember { mutableStateOf(0f) }

    var rainCoordinates by remember { mutableStateOf(Rect.Zero) }
    var heroCoordinates by remember { mutableStateOf(Rect.Zero) }
    var coinCoordinates by remember { mutableStateOf(Rect.Zero) }

    var score by remember { mutableStateOf(0) }

    val isCollisionHeroEnemy = areComposablesOverlapping(
        composable1Bounds = rainCoordinates,
        composable2Bounds = heroCoordinates,
    )
    val isCollisionHeroCoin = areComposablesOverlapping(
        composable1Bounds = coinCoordinates,
        composable2Bounds = heroCoordinates,
    )

    val fullBackground by animateColorAsState(
        targetValue = if (isCollisionHeroEnemy) Color.Red else Color.White,
    )

    LaunchedEffect(isCollisionHeroCoin) {
        if (isCollisionHeroCoin) {
            score++
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged {
                screenHeightPx = it.height.toFloat()
                screenWidthPx = it.width.toFloat()
            }
            .background(fullBackground),
    ) {
        EndlessBackground(
            asset = MR.images.symbol_bg,
            screenHeightPx = screenHeightPx,
        )
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(MR.images.white_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Text(
            modifier = Modifier.align(Alignment.TopEnd),
            text = "Score: $score",
        )
        FallingCoin(
            modifier = Modifier,
            screenHeightPx = screenHeightPx,
            screenWidthPx = screenWidthPx,
            coinCoordinates = { coordinates ->
                coinCoordinates = coordinates
            },
        )
        FallingRain(
            modifier = Modifier.align(Alignment.TopCenter),
            screenHeightPx = screenHeightPx,
            rainCoordinates = { coordinates ->
                rainCoordinates = coordinates
            },
        )

        DraggableHero(
            heroCoordinates = { coordinates ->
                heroCoordinates = coordinates
            },
        )
    }
}

@Composable
private fun DraggableHero(
    modifier: Modifier = Modifier,
    heroCoordinates: (Rect) -> Unit,
) {
    val density = LocalDensity.current
    var offsetX by remember { mutableStateOf(0f) }
    var direction by remember { mutableStateOf(HeroDirection.RIGHT) }
    val heroSize by remember { mutableStateOf(100.dp) }
    var endBound by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .onSizeChanged { endBound = it.width.toFloat() - with(density) { heroSize.toPx() } }
            .fillMaxSize()
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .padding(bottom = 50.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    direction = when {
                        dragAmount.x < -2f -> HeroDirection.LEFT
                        dragAmount.x > 2f -> HeroDirection.RIGHT
                        else -> direction
                    }
                    val dragWithBounds = (offsetX + dragAmount.x).coerceIn(
                        minimumValue = 0f,
                        maximumValue = endBound,
                    )
                    offsetX = dragWithBounds
                }
            },
        contentAlignment = Alignment.BottomStart,
    ) {
        AnimatedHero(
            modifier = Modifier
                .onGloballyPositioned {
                    heroCoordinates(it.boundsInRoot())
                }
                .graphicsLayer {
                    rotationY = when (direction) {
                        HeroDirection.LEFT -> 180f
                        HeroDirection.RIGHT -> 0f
                    }
                }
                .size(heroSize),
            assets = mainHeroAssets,
        )
    }
}

enum class HeroDirection {
    LEFT,
    RIGHT,
}

@Composable
fun FallingCoin(
    modifier: Modifier = Modifier,
    screenHeightPx: Float,
    screenWidthPx: Float,
    coinCoordinates: (Rect) -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val density = LocalDensity.current
    val coinSizeDp = 50.dp
    val coinSizePx = with(density) { coinSizeDp.toPx() }

    val y by infiniteTransition.animateValue(
        initialValue = -coinSizePx,
        targetValue = screenHeightPx,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        typeConverter = Float.VectorConverter,
    )
    val x = remember { Animatable(0f) }

    LaunchedEffect(screenWidthPx, coinSizePx) {
        while (coinSizePx > 0 && screenWidthPx > 0) {
            x.animateTo(
                targetValue = (0..(screenWidthPx.toInt() - coinSizePx.toInt())).random().toFloat(),
                animationSpec = tween(500),
            )
            delay(2000)
        }
    }

    AnimatedHero(
        modifier = modifier
            .size(50.dp)
            .graphicsLayer {
                translationY = y
                translationX = x.value
            }
            .onGloballyPositioned { coordinates ->
                coinCoordinates(coordinates.boundsInRoot())
            },
        assets = coinAssets,
    )
}

@Composable
fun FallingRain(
    modifier: Modifier = Modifier,
    screenHeightPx: Float,
    rainCoordinates: (Rect) -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Green,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    val y by infiniteTransition.animateValue(
        initialValue = 0f,
        targetValue = screenHeightPx,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        typeConverter = Float.VectorConverter,
    )

    Box(
        modifier = modifier
            .size(100.dp)
            .graphicsLayer {
                translationY = y
            }
            .background(color)
            .onGloballyPositioned { coordinates ->
                rainCoordinates(coordinates.boundsInRoot())
            },
    )
}

fun areComposablesOverlapping(
    composable1Bounds: Rect,
    composable2Bounds: Rect,
): Boolean {
    // Check for horizontal overlap
    val horizontalOverlap = (
        composable1Bounds.left <= composable2Bounds.right &&
            composable1Bounds.right >= composable2Bounds.left
        )

    // Check for vertical overlap
    val verticalOverlap = (
        composable1Bounds.top <= composable2Bounds.bottom &&
            composable1Bounds.bottom >= composable2Bounds.top
        )

    // Return true if both horizontal and vertical overlap is true
    return horizontalOverlap && verticalOverlap
}
