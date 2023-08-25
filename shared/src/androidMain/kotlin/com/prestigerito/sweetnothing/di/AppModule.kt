package com.prestigerito.sweetnothing.di

import android.content.Context
import com.prestigerito.sweetnothing.core.data.DatabaseDriverFactory
import com.prestigerito.sweetnothing.data.SqlDelightScoreDataSource
import com.prestigerito.sweetnothing.database.GameDatabase
import com.prestigerito.sweetnothing.domain.ScoreDataSource

actual class AppModule(
    private val context: Context,
) {
    actual val scoreDataSource: ScoreDataSource by lazy {
        SqlDelightScoreDataSource(
            db = GameDatabase(
                driver = DatabaseDriverFactory(context).create(),
            )
        )
    }
}