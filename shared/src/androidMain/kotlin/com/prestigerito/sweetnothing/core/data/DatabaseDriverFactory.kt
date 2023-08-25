package com.prestigerito.sweetnothing.core.data

import android.content.Context
import com.prestigerito.sweetnothing.database.GameDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(
    private val context: Context,
) {
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(
            GameDatabase.Schema,
            context,
            "game.db",
        )
    }
}
