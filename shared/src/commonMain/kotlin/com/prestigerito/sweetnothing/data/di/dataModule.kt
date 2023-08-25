package com.prestigerito.sweetnothing.data.di

import com.prestigerito.sweetnothing.data.SqlDelightScoreDataSource
import com.prestigerito.sweetnothing.database.GameDatabase
import com.prestigerito.sweetnothing.domain.ScoreDataSource
import org.koin.dsl.module

val dataModule = module {
    single<ScoreDataSource> { SqlDelightScoreDataSource(db = GameDatabase(driver = get())) }
}
