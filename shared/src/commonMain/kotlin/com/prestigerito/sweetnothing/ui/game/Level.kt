package com.prestigerito.sweetnothing.ui.game

data class Level(
    val index: Int = 1,
    val enemySpeed: Int = 2000,
    val coinSpeed: Int = 2000,
) {
    fun increase(newLevelIndex: Int): Level {
        if (index == newLevelIndex) return this
        return this.copy(
            index = this.index + 1,
            enemySpeed = this.enemySpeed + 500,
            coinSpeed = this.enemySpeed + 1000,
        )
    }
}
