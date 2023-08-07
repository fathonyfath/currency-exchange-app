package dev.fathony.currencyexchange.db

import app.cash.sqldelight.db.SqlDriver

internal expect class DriverFactory {

    fun createDriver(dbName: String): SqlDriver
}