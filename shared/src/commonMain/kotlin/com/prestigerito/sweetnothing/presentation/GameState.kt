package com.prestigerito.sweetnothing.presentation

data class GameState(
    val coin: FallingItemSpec = FallingItemSpec(
        speed = 2000,
        amount = 5,
    ),
    val enemy1: FallingItemSpec = FallingItemSpec(
        speed = 1700,
        amount = 2,
    ),
    val enemy2: FallingItemSpec = FallingItemSpec(
        speed = 1600,
        amount = 0,
    ),
    val isGameInProgress: Boolean = true,
    val levelState: LevelState = LevelState.Level(1),
    val isLoading: Boolean = true,
)

sealed interface LevelState {
    data class Level(val index: Int) : LevelState
    data class NewRecord(val highScore: Int) : LevelState
    data object TryAgain : LevelState
}

data class FallingItemSpec(
    val speed: Int,
    val amount: Int,
)
