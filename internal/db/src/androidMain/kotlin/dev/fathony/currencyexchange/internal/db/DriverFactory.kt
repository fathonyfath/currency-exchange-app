package dev.fathony.currencyexchange.internal.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.fathony.currencyexchange.internal.db.sqldelight.Database

actual class DriverFactory
constructor(private val context: Context) {

    actual fun createDriver(dbName: String): SqlDriver {
        return AndroidSqliteDriver(Database.Schema, context, dbName)
    }
}
