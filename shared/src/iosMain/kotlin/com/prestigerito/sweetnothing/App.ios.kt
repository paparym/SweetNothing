package com.prestigerito.sweetnothing

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.prestigerito.sweetnothing.navigation.RootComponent
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

fun MainViewController() = ComposeUIViewController {
    val isDarkTheme = UIScreen.mainScreen.traitCollection.userInterfaceStyle ==
        UIUserInterfaceStyle.UIUserInterfaceStyleDark
    val root = remember {
        RootComponent(DefaultComponentContext(LifecycleRegistry()))
    }
    AppScreen(
        darkTheme = isDarkTheme,
        dynamicColor = false,
        root = root,
    )
}
