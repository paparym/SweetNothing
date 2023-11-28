package com.prestigerito.sweetnothing.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prestigerito.sweetnothing.MR
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun RuleButton(
    text: String,
    onClick: (() -> Unit)? = null,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val borderColor by infiniteTransition.animateColor(
        initialValue = Color.Gray,
        targetValue = Color.DarkGray,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    val gradientChoreographyList = listOf(
        btn_gradient_blue_light,
        btn_gradient_blue_dark,
        btn_gradient_blue_light,
    )
    BoxWithConstraints {
        val gradientValue by infiniteTransition.animateFloat(
            initialValue = 0.0f,
            targetValue = constraints.maxWidth.toFloat() * 2,
            animationSpec = infiniteRepeatable(
                animation = tween(10000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
        )
        Box(
            modifier = Modifier
                .size(200.dp, 80.dp)
                .border(
                    border = BorderStroke(width = 3.dp, color = Color.Black),
                    shape = RoundedCornerShape(12.dp),
                )
                .background(
                    brush = Brush.horizontalGradient(
                        colors = gradientChoreographyList,
                        startX = 0.0f,
                        endX = gradientValue,
                    ),
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    border = BorderStroke(width = 6.dp, color = borderColor),
                    shape = RoundedCornerShape(12.dp),
                )
                .clickableOrNot(onClick),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val resource = if (enabled) MR.images.blue_bar else MR.images.grey_bar
    MenuButtonLayout(
        image = {
            Image(
                modifier = Modifier.then(if (enabled) Modifier.clickable { onClick.invoke() } else Modifier),
                painter = painterResource(imageResource = resource),
                contentDescription = null,
            )
        },
        text = {
            Text(
                modifier = Modifier.defaultMinSize(),
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        },
    )
}

@Composable
private fun MenuButtonLayout(
    modifier: Modifier = Modifier,
    image: @Composable () -> Unit,
    text: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        contents = listOf(image, text),
    ) { (imageMeasurables, textMeasurables), constraints ->
        val textPlaceable = textMeasurables.first().measure(
            constraints.copy(
                maxWidth = constraints.maxWidth * 4,
                maxHeight = constraints.maxHeight * 4,
            ),
        )
        val imagePlaceable = imageMeasurables.first().measure(
            constraints.copy(
                maxWidth = textPlaceable.width * 4,
                maxHeight = textPlaceable.height * 4,
            ),
        )
        layout(
            width = imagePlaceable.width,
            height = imagePlaceable.height,
        ) {
            imagePlaceable.placeRelative(0, 0)
//            textPlaceable.place(imagePlaceable.width / 2, imagePlaceable.height / 2)
            textPlaceable.placeRelative(
                x = (imagePlaceable.width / 2) - textPlaceable.width / 2,
                y = (imagePlaceable.height / 2) - textPlaceable.height / 2,
            )
        }
    }
}

@Composable
fun Modifier.clickableOrNot(onClick: (() -> Unit)?): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return if (onClick == null) {
        this.clickable(
            interactionSource = interactionSource,
            indication = null,
        ) {
            // no op
        }
    } else {
        this.clickable(onClick = onClick)
    }
}
