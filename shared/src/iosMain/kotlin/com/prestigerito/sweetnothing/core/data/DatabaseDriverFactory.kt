package com.prestigerito.sweetnothing.core.data

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.prestigerito.sweetnothing.database.GameDatabase

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return NativeSqliteDriver(
            GameDatabase.Schema,
            "game.db",
        )
    }
}
