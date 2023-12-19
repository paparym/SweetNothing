package com.prestigerito.sweetnothing.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.prestigerito.sweetnothing.presentation.game.GameComponent
import com.prestigerito.sweetnothing.presentation.highscores.HighScoreComponent
import com.prestigerito.sweetnothing.presentation.menu.MainMenuComponent
import com.prestigerito.sweetnothing.presentation.rules.RulesComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.serialization.Serializable
import kotlin.coroutines.CoroutineContext

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.MainMenu,
        handleBackButton = true,
        childFactory = ::createChild,
    )

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(
        config: Configuration,
        context: ComponentContext,
    ): Child {
        return when (config) {
            Configuration.MainMenu -> Child.MainMenu(
                component = MainMenuComponent(
                    componentContext = context,
                    onPlayClicked = { navigation.pushNew(Configuration.Game) },
                    onHighScoresClicked = { navigation.pushNew(Configuration.HighScores) },
                    onRulesClicked = { navigation.pushNew(Configuration.Rules) },
                ),
            )

            Configuration.Game -> Child.Game(
                component = GameComponent(
                    componentContext = context,
                    mainContext = Dispatchers.Main,
                    onBack = { navigation.pop() },
                ),
            )

            Configuration.HighScores -> Child.HighScores(
                component = HighScoreComponent(
                    componentContext = context,
                    mainContext = Dispatchers.Main,
                    onBack = { navigation.pop() },
                ),
            )

            Configuration.Rules -> Child.Rules(
                component = RulesComponent(
                    componentContext = context,
                    onStartGame = { navigation.pushNew(Configuration.Game) },
                    onBack = { navigation.pop() },
                ),
            )
        }
    }

    sealed class Child {
        data class MainMenu(val component: MainMenuComponent) : Child()
        data class Game(val component: GameComponent) : Child()
        data class Rules(val component: RulesComponent) : Child()
        data class HighScores(val component: HighScoreComponent) : Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object MainMenu : Configuration()

        @Serializable
        data object Game : Configuration()

        @Serializable
        data object Rules : Configuration()

        @Serializable
        data object HighScores : Configuration()
    }
}

fun CoroutineScope(context: CoroutineContext, lifecycle: Lifecycle): CoroutineScope {
    val scope = CoroutineScope(context)
    lifecycle.doOnDestroy(scope::cancel)
    return scope
}

fun LifecycleOwner.coroutineScope(context: CoroutineContext): CoroutineScope =
    CoroutineScope(context, lifecycle)
