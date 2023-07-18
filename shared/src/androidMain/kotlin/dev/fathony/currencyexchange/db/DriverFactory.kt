package dev.fathony.currencyexchange.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.fathony.currencyexchange.internal.DatabaseName
import dev.fathony.currencyexchange.sqldelight.Database

internal actual class DriverFactory(private val context: Context) {

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(Database.Schema, context, DatabaseName)
    }
}
