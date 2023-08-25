package com.prestigerito.sweetnothing.presentation

import com.prestigerito.sweetnothing.domain.ScoreDataSource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MainMenuViewModel(
    private val scoreDataSource: ScoreDataSource,
) : ViewModel() {
    private val _state = MutableStateFlow(MainMenuState())
    val state = combine(
        _state,
        scoreDataSource.getAllScores(),
    ) { state, scores ->
        state.copy(scores = scores)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MainMenuState())

    fun addScoreToDb() {
        scoreDataSource.saveScore((1..100).random())
    }
}
