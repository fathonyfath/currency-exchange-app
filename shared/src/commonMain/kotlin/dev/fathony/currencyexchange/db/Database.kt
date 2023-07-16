package dev.fathony.currencyexchange.db

import dev.fathony.currencyexchange.sqldelight.Database

fun createDatabase(driverFactory: DriverFactory): Database {
    val driver = driverFactory.createDriver()
    return Database(driver)
}
