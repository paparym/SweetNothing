package com.prestigerito.sweetnothing.domain

import kotlinx.coroutines.flow.Flow

interface ScoreDataSource {
    suspend fun saveScore(score: Score)
    fun getAllScores(): Flow<List<Score>>
}
