package com.prestigerito.sweetnothing.presentation

import com.prestigerito.sweetnothing.domain.ScoreDataSource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class HighScoreViewModel : ViewModel(), KoinComponent {
    private val scoreDataSource: ScoreDataSource = get()
    private val _state = MutableStateFlow(HighScoreState())
    val state = combine(
        _state,
        scoreDataSource.getAllScores(),
    ) { state, scores ->
        state.copy(score = scores.firstOrNull())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HighScoreState())
}
