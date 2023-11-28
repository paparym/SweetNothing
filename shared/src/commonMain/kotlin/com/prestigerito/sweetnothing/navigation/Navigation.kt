package com.prestigerito.sweetnothing.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.prestigerito.sweetnothing.presentation.GameViewModel
import com.prestigerito.sweetnothing.presentation.HighScoreViewModel
import com.prestigerito.sweetnothing.presentation.MainMenuViewModel
import com.prestigerito.sweetnothing.ui.game.GameScreen
import com.prestigerito.sweetnothing.ui.highscore.HighScoreScreen
import com.prestigerito.sweetnothing.ui.menu.MainMenu
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

object MainScreenNav : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getViewModel(
            key = "main-menu-screen",
            factory = viewModelFactory {
                MainMenuViewModel()
            },
        )
        MainMenu(
            viewModel = viewModel,
            onPlayClicked = { navigator.push(GameScreenNav) },
            onSelectLevelClicked = { navigator.push(GameScreenNav) },
            onHighScoresClicked = { navigator.push(HighScoreScreenNav) },
        )
    }
}

object GameScreenNav : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getViewModel(
            key = "game-screen",
            factory = viewModelFactory {
                GameViewModel()
            },
        )
        GameScreen(
            viewModel = viewModel,
            onBack = { navigator.pop() },
        )
    }
}

object HighScoreScreenNav : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getViewModel(
            key = "high-score-screen",
            factory = viewModelFactory {
                HighScoreViewModel()
            },
        )
        HighScoreScreen(
            viewModel = viewModel,
            onBack = { navigator.pop() },
        )
    }
}
