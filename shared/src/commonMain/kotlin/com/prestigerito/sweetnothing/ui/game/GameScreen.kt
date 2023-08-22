package com.prestigerito.sweetnothing.ui.game

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun GameScreen() {
    var screenHeightPx by remember { mutableStateOf(0f) }
    var screenWidthPx by remember { mutableStateOf(0f) }

    var rainCoordinates by remember { mutableStateOf(Rect.Zero) }
    var heroCoordinates by remember { mutableStateOf(Rect.Zero) }

    val isCollision = areComposablesOverlapping(
        composable1Bounds = rainCoordinates,
        composable2Bounds = heroCoordinates,
    )

    val fullBackground by animateColorAsState(
        targetValue = if (isCollision) Color.Red else Color.White,
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged {
                screenHeightPx = it.height.toFloat()
                screenWidthPx = it.width.toFloat()
            }
            .background(fullBackground),
    ) {
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
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .background(MaterialTheme.colorScheme.primary)
            .size(50.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }.onGloballyPositioned {
                heroCoordinates(it.boundsInRoot())
            },
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
