package com.prestigerito.sweetnothing.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.prestigerito.sweetnothing.MR
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun Typography() = Typography(
    bodyLarge = TextStyle(
        fontFamily = fontFamilyResource(MR.fonts.vina_sans.vina_sans),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    )
)