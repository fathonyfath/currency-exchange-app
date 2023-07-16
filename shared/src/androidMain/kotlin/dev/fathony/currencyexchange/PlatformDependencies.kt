package dev.fathony.currencyexchange

import android.content.Context
import dev.fathony.currencyexchange.db.DriverFactory
import dev.fathony.currencyexchange.sqldelight.Database

actual class PlatformDependencies(private val context: Context) {

    private val driverFactory: DriverFactory = DriverFactory(context)

    internal actual fun createDatabase(): Database {
        return Database(driverFactory.createDriver())
    }
}
