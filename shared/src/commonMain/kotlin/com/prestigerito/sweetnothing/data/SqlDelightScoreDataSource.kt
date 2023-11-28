package com.prestigerito.sweetnothing.data

import com.prestigerito.sweetnothing.database.GameDatabase
import com.prestigerito.sweetnothing.domain.Score
import com.prestigerito.sweetnothing.domain.ScoreDataSource
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SqlDelightScoreDataSource(
    db: GameDatabase,
) : ScoreDataSource {

    private val queries = db.scoreQueries
    override suspend fun saveScore(score: Score) {
        queries.insertScore(
            // so far, we store only one result
            id = 0,
            score = score.highScore.toLong(),
            levelIndex = score.levelIndex.toLong(),
        )
    }

    override fun getAllScores(): Flow<List<Score>> {
        return queries.getAllScores()
            .asFlow()
            .mapToList()
            .map { scoreEntities ->
                scoreEntities.map {
                    Score(
                        highScore = it.score.toInt(),
                        levelIndex = it.levelIndex.toInt(),
                    )
                }
            }
    }
}
