package dev.fathony.currencyexchange.internal.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import dev.fathony.currencyexchange.internal.db.sqldelight.Database

actual class DriverFactory {

    actual fun createDriver(dbName: String): SqlDriver {
        return NativeSqliteDriver(Database.Schema, dbName)
    }
}
