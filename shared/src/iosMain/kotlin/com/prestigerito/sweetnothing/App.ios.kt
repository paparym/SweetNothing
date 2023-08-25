package com.prestigerito.sweetnothing

import androidx.compose.ui.window.ComposeUIViewController
import com.prestigerito.sweetnothing.di.AppModule
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

fun MainViewController() = ComposeUIViewController {
    val isDarkTheme = UIScreen.mainScreen.traitCollection.userInterfaceStyle ==
        UIUserInterfaceStyle.UIUserInterfaceStyleDark
    AppScreen(
        darkTheme = isDarkTheme,
        dynamicColor = false,
        appModule = AppModule(),
    )
}
