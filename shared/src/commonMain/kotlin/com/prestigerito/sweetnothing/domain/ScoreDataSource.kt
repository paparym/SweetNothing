package com.prestigerito.sweetnothing.domain

import kotlinx.coroutines.flow.Flow

interface ScoreDataSource {
    fun saveScore(score: Int)
    fun getAllScores(): Flow<List<Int>>
}