package dev.fathony.currencyexchange.db

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory {

    fun createDriver(): SqlDriver
}
