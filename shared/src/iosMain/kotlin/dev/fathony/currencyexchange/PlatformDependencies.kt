package dev.fathony.currencyexchange

import dev.fathony.currencyexchange.db.DriverFactory
import dev.fathony.currencyexchange.sqldelight.Database

actual class PlatformDependencies {

    private val driverFactory: DriverFactory = DriverFactory()

    internal actual fun createDatabase(): Database {
        return Database(driverFactory.createDriver())
    }
}
