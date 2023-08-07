package dev.fathony.currencyexchange.internal.db

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory {

    fun createDriver(dbName: String): SqlDriver
}
