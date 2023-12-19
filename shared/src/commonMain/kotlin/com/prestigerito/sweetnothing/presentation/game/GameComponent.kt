package com.prestigerito.sweetnothing.presentation.game

import com.arkivanov.decompose.ComponentContext
import com.prestigerito.sweetnothing.domain.Score
import com.prestigerito.sweetnothing.domain.ScoreDataSource
import com.prestigerito.sweetnothing.navigation.coroutineScope
import com.prestigerito.sweetnothing.presentation.GameState
import com.prestigerito.sweetnothing.presentation.LevelState
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import kotlin.coroutines.CoroutineContext
import kotlin.math.floor

class GameComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
    mainContext: CoroutineContext,
) : ComponentContext by componentContext, KoinComponent {
    private val scope = coroutineScope(mainContext + SupervisorJob())
    private val scoreDataSource: ScoreDataSource = get()
    private val _state = MutableStateFlow(GameState())
    private val _score = MutableStateFlow(0)

    val score = _score.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0,
    )

    val state = _state.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GameState(),
    )

    fun addScore() {
        val originalSpeed = 2000
        val levelIndex = floor((_score.value / 10).toFloat()).toInt().coerceAtLeast(1)
        val coinSpeed = originalSpeed - (levelIndex * 50)
        val enemy1Speed = originalSpeed - (levelIndex * 100)
        val enemy1Amount = levelIndex
        val enemy2Speed = originalSpeed - (levelIndex * 100)
        val enemy2Amount = levelIndex - 3
        _score.update { it + 1 }
        _state.update {
            it.copy(
                levelState = LevelState.Level(index = levelIndex),
                coin = it.coin.copy(speed = coinSpeed),
                enemy1 = it.enemy1.copy(speed = enemy1Speed, amount = enemy1Amount),
                enemy2 = it.enemy2.copy(speed = enemy2Speed, amount = enemy2Amount),
            )
        }
    }

    fun gameFinished() {
        val levelState = (_state.value.levelState as LevelState.Level)
        scope.launch {
            val currentHighScore = scoreDataSource.getAllScores().first().firstOrNull()
            if ((currentHighScore?.highScore ?: 0) < _score.value) {
                scoreDataSource.saveScore(
                    score = Score(
                        highScore = _score.value,
                        levelIndex = levelState.index,
                    ),
                )
                _state.update {
                    it.copy(
                        isGameInProgress = false,
                        levelState = LevelState.NewRecord(highScore = _score.value),
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isGameInProgress = false,
                        levelState = LevelState.TryAgain,
                    )
                }
            }
        }
    }

    fun refreshGame() {
        _state.update { GameState(isLoading = false) }
        _score.update { 0 }
    }

    fun goBack() = onBack.invoke()
    fun enableGame() = _state.update { it.copy(isLoading = false) }
}
