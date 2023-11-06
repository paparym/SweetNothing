package com.prestigerito.sweetnothing.ui.game

import androidx.compose.animation.animateColor
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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.prestigerito.sweetnothing.ui.menu.AnimatedItem
import com.prestigerito.sweetnothing.ui.menu.AnimationType
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun GameScreen() {
    var heroCoordinates by remember { mutableStateOf(Rect.Zero) }

    var score by remember { mutableStateOf(0) }

//    val backgroundColor by animateColorAsState(
//        targetValue = when {
//            heroCoordinates.overlaps(coinCoordinates) -> Color.Red
//            isCollisionHeroCoin -> Color.Yellow
//            else -> Color.White
//        },
//    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
//            .background(backgroundColor)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        Image(
            modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars),
            painter = painterResource(MR.images.white_bg),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
        )
        Text(
            modifier = Modifier.align(Alignment.TopEnd),
            text = "Score: $score",
        )

        repeat(10) {
            FallingCoin(
                modifier = Modifier,
                screenHeightPx = constraints.maxHeight.toFloat(),
                screenWidthPx = constraints.maxWidth.toFloat(),
                heroCoordinates = heroCoordinates,
                onCollision = {
                    score++
                }
            )
        }

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
        AnimatedItem(
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
            animationType = AnimationType.NO_ANIMATION,
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
    heroCoordinates: Rect,
    onCollision: () -> Unit,
) {
    val density = LocalDensity.current
    val coinSizeDp = 50.dp
    val coinSizePx = with(density) { coinSizeDp.toPx() }

    val yAnimation = remember {
        Animatable(-coinSizePx)
    }

    fun randomHorizontalPosition() =
        (0..(screenWidthPx.toInt() - coinSizePx.toInt())).random().toFloat()

    val xAnimation = remember {
        Animatable(randomHorizontalPosition())
    }

    val coroutineScope = rememberCoroutineScope()
    var runAgain by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) {
            yAnimation.animateTo(
                targetValue = screenHeightPx,
                animationSpec = tween(
                    durationMillis = 2000,
                    easing = LinearEasing
                )
            )
            xAnimation.snapTo(targetValue = randomHorizontalPosition())
            yAnimation.snapTo(targetValue = -coinSizePx)
        }
    }

    AnimatedItem(
        modifier = modifier
            .size(50.dp)
            .graphicsLayer {
                translationY = yAnimation.value
                translationX = xAnimation.value
            }
            .onGloballyPositioned { coordinates ->
                if (coordinates.boundsInRoot().overlaps(heroCoordinates)) {
                    coroutineScope.launch {
                        yAnimation.snapTo(-coinSizePx)
                        xAnimation.snapTo(targetValue = randomHorizontalPosition())
                        runAgain = !runAgain
                        onCollision.invoke()
                    }
                }
            },
        assets = coinAssets,
        animationType = AnimationType.Y_AXIS_ROTATION,
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

fun areComposablesOverlapping(
    composable1BoundsList: List<Rect>,
    composable2Bounds: Rect,
): Boolean {
    for (composable1Bound in composable1BoundsList) {
        if (composable1Bound.overlaps(composable2Bounds)) {
            return true
        }
    }
    return false
}
