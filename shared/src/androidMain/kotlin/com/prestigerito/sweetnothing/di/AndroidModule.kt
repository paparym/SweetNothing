package com.prestigerito.sweetnothing.di

import com.prestigerito.sweetnothing.database.GameDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.engine.android.Android
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { Android.create() }
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = GameDatabase.Schema,
            context = androidContext(),
            name = "game.db",
        )
    }
}
