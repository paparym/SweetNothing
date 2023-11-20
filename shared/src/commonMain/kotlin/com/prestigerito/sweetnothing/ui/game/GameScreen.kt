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
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
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
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun GameScreen(
    onBack: () -> Unit,
) {
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
//            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
//                .windowInsetsPadding(WindowInsets.systemBars),
            painter = painterResource(MR.images.white_bg),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .windowInsetsPadding(WindowInsets.systemBars),
            text = "Score: $score",
        )
        // Coins
        repeat(10) {
            FallingItem(
                modifier = Modifier,
                screenHeightPx = constraints.maxHeight.toFloat(),
                screenWidthPx = constraints.maxWidth.toFloat(),
                heroCoordinates = heroCoordinates,
                startDelay = it * 200L,
                animationType = AnimationType.Y_AXIS_ROTATION,
                assets = coinAssets,
                speed = if (score > 20) 2000 else 3000,
                gameInProgress = score <= 10,
                onCollision = {
                    score++
                }
            )
        }

        Icon(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(16.dp)
                .clickable { onBack.invoke() },
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null
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
fun FallingItem(
    modifier: Modifier = Modifier,
    screenHeightPx: Float,
    screenWidthPx: Float,
    heroCoordinates: Rect,
    startDelay: Long = 0L,
    animationType: AnimationType = AnimationType.Y_AXIS_ROTATION,
    assets: List<ImageResource> = coinAssets,
    speed: Int = 2000,
    gameInProgress: Boolean = true,
    onCollision: () -> Unit,
) {
    val density = LocalDensity.current
    val itemSizeDp = 50.dp
    val itemSizePx = with(density) { itemSizeDp.toPx() }

    val yAnimation = remember {
        Animatable(-itemSizePx)
    }

    fun randomHorizontalPosition() =
        (0..(screenWidthPx.toInt() - itemSizePx.toInt())).random().toFloat()

    val xAnimation = remember {
        Animatable(randomHorizontalPosition())
    }
    val itemAlpha = remember {
        Animatable(0f)
    }

    val coroutineScope = rememberCoroutineScope()
    var itemCoordinates by remember { mutableStateOf(Rect.Zero) }
    var fallingToggle by remember { mutableStateOf(AnimationHelper.START) }
    var alphaToggle by remember { mutableStateOf(true) }

    LaunchedEffect(fallingToggle) {
        if (fallingToggle == AnimationHelper.START) {
            delay(startDelay)
        }
        while (gameInProgress) {
            yAnimation.animateTo(
                targetValue = screenHeightPx,
                animationSpec = tween(
                    durationMillis = speed,
                    easing = LinearEasing
                )
            )
            xAnimation.snapTo(targetValue = randomHorizontalPosition())
            yAnimation.snapTo(targetValue = -itemSizePx)
            itemAlpha.snapTo(targetValue = 0f)
            alphaToggle = !alphaToggle
        }
    }
    LaunchedEffect(alphaToggle) {
        itemAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 350,
                easing = LinearEasing,
            )
        )
    }
    LaunchedEffect(gameInProgress) {
        if (!gameInProgress) {
            yAnimation.animateTo(yAnimation.value)
            itemAlpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 1000,
                    delayMillis = 500,
                )
            )
        }
    }

    AnimatedItem(
        modifier = modifier
            .size(50.dp)
            .graphicsLayer {
                translationY = yAnimation.value
                translationX = xAnimation.value
                alpha = itemAlpha.value
            }
            .onGloballyPositioned { coordinates ->
                itemCoordinates = coordinates.boundsInRoot()
                if (coordinates.boundsInRoot().overlaps(heroCoordinates) && gameInProgress) {
                    coroutineScope.launch {
                        yAnimation.snapTo(-itemSizePx)
                        xAnimation.snapTo(targetValue = randomHorizontalPosition())
                        itemAlpha.snapTo(targetValue = 0f)
                        onCollision.invoke()
                        // start falling again
                        fallingToggle = fallingToggle.invert()
                        alphaToggle = !alphaToggle
                    }
                }
            },
        assets = assets,
        animationType = animationType,
    )
}

enum class AnimationHelper {
    START, IN_PROGRESS_TOGGLE_ON, IN_PROGRESS_TOGGLE_OFF;

    fun invert(): AnimationHelper {
        return if (this == IN_PROGRESS_TOGGLE_ON) {
            IN_PROGRESS_TOGGLE_OFF
        } else {
            IN_PROGRESS_TOGGLE_ON
        }
    }
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
