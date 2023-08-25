package com.prestigerito.sweetnothing.di

import com.prestigerito.sweetnothing.database.GameDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

val appModule = module {
    single { Darwin.create() }
    single<SqlDriver> {
        NativeSqliteDriver(
            GameDatabase.Schema,
            "game.db",
        )
    }
}
