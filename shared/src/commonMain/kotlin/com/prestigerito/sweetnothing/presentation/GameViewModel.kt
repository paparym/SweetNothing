package com.prestigerito.sweetnothing.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent
import kotlin.math.floor

class GameViewModel : ViewModel(), KoinComponent {
    private val _state = MutableStateFlow(GameState())
    private val _score = MutableStateFlow(0)

    val score = _score.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0,
    )

    val state = combine(
        _score,
        _state,
    ) { score, state ->
        val originalSpeed = 2000
        val level = floor((score / 10).toFloat()).toInt().coerceAtLeast(1)
        val coinSpeed = originalSpeed - (level * 50)
        val enemy1Speed = originalSpeed - (level * 100)
        val enemy1Amount = level
        val enemy2Speed = originalSpeed - (level * 100)
        val enemy2Amount = level - 3
        state.copy(
            level = level,
            coin = state.coin.copy(speed = coinSpeed),
            enemy1 = state.enemy1.copy(speed = enemy1Speed, amount = enemy1Amount),
            enemy2 = state.enemy2.copy(speed = enemy2Speed, amount = enemy2Amount),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GameState(),
    )

    fun addScore() {
        _score.update { it + 1 }
    }

    private fun updatedLevel(score: Int): GameState {
        return when (score) {
            in 0..10 -> _state.value.copy(level = 1)
            in 11..20 -> _state.value.copy(level = 2)
            in 21..30 -> _state.value.copy(level = 3)
            else -> _state.value.copy(level = 50)
        }
    }

//    private fun updatedCoin(level: Int): GameState {
//        return when (level) {
//            1 -> _state.value.copy(
//                coin = _state.value.coin.copy(speed =)
//            )
//
//            in 11..20 -> _state.value.copy(level = 2)
//            in 21..30 -> _state.value.copy(level = 3)
//            else -> _state.value.copy(level = 50)
//        }
//    }
}
