package com.prestigerito.sweetnothing.presentation.menu

import com.arkivanov.decompose.ComponentContext
import com.prestigerito.sweetnothing.domain.ScoreDataSource
import com.prestigerito.sweetnothing.presentation.MainMenuState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MainMenuComponent(
    componentContext: ComponentContext,
    private val onPlayClicked: () -> Unit,
    private val onHighScoresClicked: () -> Unit,
    private val onRulesClicked: () -> Unit,
) : ComponentContext by componentContext, KoinComponent {
    private val scoreDataSource: ScoreDataSource = get()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(MainMenuState())
    val state = combine(
        _state,
        scoreDataSource.getAllScores(),
    ) { state, scores ->
        val isHighScoreEnabled = scores.isNotEmpty()
        state.copy(highScoreAvailable = isHighScoreEnabled)
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(5000), MainMenuState())

    fun onPlayClicked() = onPlayClicked.invoke()
    fun onHighScoresClicked() = onHighScoresClicked.invoke()
    fun onRulesClicked() = onRulesClicked.invoke()
}
