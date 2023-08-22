package com.prestigerito.sweetnothing.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.prestigerito.sweetnothing.ui.game.GameScreen
import com.prestigerito.sweetnothing.ui.menu.MainMenu

object MainScreenNav : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        MainMenu(text = "Go next", onGoNextClick = { navigator.push(GameScreenNav) })
    }
}

object GameScreenNav : Screen {
    @Composable
    override fun Content() {
        GameScreen()
    }
}

sealed class SharedScreen : ScreenProvider {
    data class PdpDetails(val text: String) : SharedScreen()
}
