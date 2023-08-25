package com.prestigerito.sweetnothing.data

import com.prestigerito.sweetnothing.database.GameDatabase
import com.prestigerito.sweetnothing.domain.ScoreDataSource
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SqlDelightScoreDataSource(
    db: GameDatabase,
) : ScoreDataSource {

    private val queries = db.scoreQueries
    override fun saveScore(score: Int) {
        queries.insertScore(id = null, score = score.toLong())
    }

    override fun getAllScores(): Flow<List<Int>> {
        return queries.getAllScores()
            .asFlow()
            .mapToList()
            .map { scoreEntities ->
                scoreEntities.map {
                    it.score.toInt()
                }
            }
    }
}