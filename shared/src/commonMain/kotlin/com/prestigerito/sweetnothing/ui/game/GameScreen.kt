package com.prestigerito.sweetnothing.ui.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.prestigerito.sweetnothing.MR
import com.prestigerito.sweetnothing.presentation.GameViewModel
import com.prestigerito.sweetnothing.ui.menu.AnimatedItem
import com.prestigerito.sweetnothing.ui.menu.AnimationType
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val score by viewModel.score.collectAsState()
    var heroCoordinates by remember { mutableStateOf(Rect.Zero) }

    val density = LocalDensity.current
    var offsetX by remember { mutableStateOf(0f) }
    var direction by remember { mutableStateOf(HeroDirection.RIGHT) }
    var endBound by remember { mutableStateOf(0f) }
    val heroSize = 60.dp
    BoxWithConstraints(
        modifier = Modifier
            .onSizeChanged { endBound = it.width.toFloat() - with(density) { heroSize.toPx() } }
            .fillMaxSize()
            .then(
                if (!state.isGameInProgress) {
                    Modifier.clickable {
                        viewModel.refreshGame()
                    }
                } else {
                    Modifier
                },
            )
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
                    if (state.isGameInProgress) {
                        offsetX = dragWithBounds
                    }
                }
            },
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(MR.images.white_bg),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .windowInsetsPadding(WindowInsets.systemBars),
            text = stringResource(MR.strings.score, score),
        )
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .windowInsetsPadding(WindowInsets.systemBars),
            text = state.levelState.stringValue(),
        )
        if (state.isGameInProgress) {
            // Enemies
            repeat(state.enemy1.amount) {
                FallingItem(
                    modifier = Modifier,
                    screenHeightPx = constraints.maxHeight.toFloat(),
                    screenWidthPx = constraints.maxWidth.toFloat(),
                    heroCoordinates = { heroCoordinates },
                    startDelay = it * 200L,
                    animationType = AnimationType.NO_ANIMATION,
                    assets = enemy1Asset,
                    speed = state.enemy1.speed,
                    onCollision = { viewModel.gameFinished() },
                )
            }
            if (state.enemy2.amount > 0) {
                repeat(state.enemy2.amount) {
                    FallingItem(
                        modifier = Modifier,
                        screenHeightPx = constraints.maxHeight.toFloat(),
                        screenWidthPx = constraints.maxWidth.toFloat(),
                        heroCoordinates = { heroCoordinates },
                        startDelay = it * 250L,
                        animationType = AnimationType.NO_ANIMATION,
                        assets = enemy2Asset,
                        speed = state.enemy2.speed,
                        onCollision = { viewModel.gameFinished() },
                    )
                }
            }
            // Coins
            repeat(state.coin.amount) {
                FallingItem(
                    modifier = Modifier,
                    screenHeightPx = constraints.maxHeight.toFloat(),
                    screenWidthPx = constraints.maxWidth.toFloat(),
                    heroCoordinates = { heroCoordinates },
                    startDelay = it * 200L,
                    animationType = AnimationType.NO_ANIMATION,
                    assets = coinAssets,
                    speed = state.coin.speed,
                    onCollision = viewModel::addScore,
                )
            }
        }

        Icon(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(16.dp)
                .clickable { onBack.invoke() },
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
        )

        DraggableHero(
            heroSize = heroSize,
            offsetX = { offsetX.roundToInt() },
            direction = direction,
            heroCoordinates = { coordinates ->
                heroCoordinates = coordinates
            },
        )
    }
}

@Composable
private fun DraggableHero(
    modifier: Modifier = Modifier,
    heroSize: Dp = 100.dp,
    offsetX: () -> Int,
    direction: HeroDirection,
    heroCoordinates: (Rect) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.invoke(), 0) }
            .padding(bottom = 50.dp),
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
    itemSizeDp: Dp = 30.dp,
    screenWidthPx: Float,
    heroCoordinates: () -> Rect,
    startDelay: Long = 0L,
    animationType: AnimationType = AnimationType.Y_AXIS_ROTATION,
    assets: List<ImageResource> = coinAssets,
    speed: Int = 2000,
    onCollision: () -> Unit,
) {
    val density = LocalDensity.current
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
        while (true) {
            yAnimation.animateTo(
                targetValue = screenHeightPx,
                animationSpec = tween(
                    durationMillis = speed,
                    easing = LinearEasing,
                ),
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
            ),
        )
    }

    AnimatedItem(
        modifier = modifier
            .size(itemSizeDp)
            .graphicsLayer {
                translationY = yAnimation.value
                translationX = xAnimation.value
                alpha = itemAlpha.value
            }
            .onGloballyPositioned { coordinates ->
                itemCoordinates = coordinates.boundsInRoot()
                if (coordinates.boundsInRoot().overlaps(heroCoordinates.invoke())) {
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
