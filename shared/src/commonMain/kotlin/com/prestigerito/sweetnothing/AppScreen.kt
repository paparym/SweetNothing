package com.prestigerito.sweetnothing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.prestigerito.sweetnothing.navigation.RootComponent
import com.prestigerito.sweetnothing.ui.AppTheme
import com.prestigerito.sweetnothing.ui.game.GameScreen
import com.prestigerito.sweetnothing.ui.highscore.HighScoreScreen
import com.prestigerito.sweetnothing.ui.menu.MainMenu
import com.prestigerito.sweetnothing.ui.rules.RulesScreen

@Composable
fun AppScreen(
    root: RootComponent,
    darkTheme: Boolean,
    dynamicColor: Boolean,
) {
    AppTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
    ) {
        val childStack by root.childStack.subscribeAsState()
        Children(
            stack = childStack,
            animation = stackAnimation(slide()),
        ) { child ->
            when (val instance = child.instance) {
                is RootComponent.Child.MainMenu -> {
                    MainMenu(
                        component = instance.component,
                    )
                }

                is RootComponent.Child.Game -> {
                    GameScreen(component = instance.component)
                }

                is RootComponent.Child.HighScores -> {
                    HighScoreScreen(component = instance.component)
                }

                is RootComponent.Child.Rules -> {
                    RulesScreen(
                        onStartGame = instance.component::onStartGame,
                        onBack = instance.component::onBack,
                    )
                }
            }
        }
    }
}
