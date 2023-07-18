package dev.fathony.currencyexchange.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import dev.fathony.currencyexchange.internal.DatabaseName
import dev.fathony.currencyexchange.sqldelight.Database

internal actual class DriverFactory {

    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(Database.Schema, DatabaseName)
    }
}
