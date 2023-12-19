package com.prestigerito.sweetnothing.presentation.highscores

import com.arkivanov.decompose.ComponentContext
import com.prestigerito.sweetnothing.domain.ScoreDataSource
import com.prestigerito.sweetnothing.navigation.coroutineScope
import com.prestigerito.sweetnothing.presentation.HighScoreState
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import kotlin.coroutines.CoroutineContext

class HighScoreComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
    mainContext: CoroutineContext,
) : ComponentContext by componentContext, KoinComponent {

    private val scoreDataSource: ScoreDataSource = get()
    private val scope = coroutineScope(mainContext + SupervisorJob())
    private val _state = MutableStateFlow(HighScoreState())
    val state = combine(
        _state,
        scoreDataSource.getAllScores(),
    ) { state, scores ->
        state.copy(score = scores.firstOrNull())
    }.stateIn(scope, SharingStarted.WhileSubscribed(5000), HighScoreState())

    fun goBack() = onBack.invoke()
}
