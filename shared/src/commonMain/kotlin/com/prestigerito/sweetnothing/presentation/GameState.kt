package com.prestigerito.sweetnothing.presentation

data class GameState(
    val level: Int = 1,
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
)

data class FallingItemSpec(
    val speed: Int,
    val amount: Int,
)
