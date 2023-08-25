package com.prestigerito.sweetnothing.di

import com.prestigerito.sweetnothing.core.data.DatabaseDriverFactory
import com.prestigerito.sweetnothing.data.SqlDelightScoreDataSource
import com.prestigerito.sweetnothing.database.GameDatabase
import com.prestigerito.sweetnothing.domain.ScoreDataSource

actual class AppModule {
    actual val scoreDataSource: ScoreDataSource by lazy {
        SqlDelightScoreDataSource(
            db = GameDatabase(
                driver = DatabaseDriverFactory().create()
            )
        )
    }
}