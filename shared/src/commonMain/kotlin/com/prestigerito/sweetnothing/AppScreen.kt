package com.prestigerito.sweetnothing

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.prestigerito.sweetnothing.navigation.GameScreenNav
import com.prestigerito.sweetnothing.ui.AppTheme

@Composable
fun AppScreen(
    darkTheme: Boolean,
    dynamicColor: Boolean,
) {
    AppTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
    ) {
        Navigator(GameScreenNav)
    }
}
