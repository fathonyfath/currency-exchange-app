package dev.fathony.currencyexchange

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import dev.fathony.currencyexchange.db.DriverFactory
import dev.fathony.currencyexchange.internal.DatabaseName
import dev.fathony.currencyexchange.internal.PlatformTimeProvider
import dev.fathony.currencyexchange.internal.SettingsName
import dev.fathony.currencyexchange.internal.TimeProvider
import dev.fathony.currencyexchange.internal.db.sqldelight.Database

actual class PlatformDependencies {

    private val driverFactory: DriverFactory = DriverFactory()

    private val database: Database by lazy(LazyThreadSafetyMode.NONE) {
        Database(driverFactory.createDriver(DatabaseName))
    }

    private val settingsFactory: Settings.Factory = NSUserDefaultsSettings.Factory()

    private val settings: Settings by lazy(LazyThreadSafetyMode.NONE) {
        settingsFactory.create(SettingsName)
    }

    private val timeProvider: TimeProvider by lazy(LazyThreadSafetyMode.NONE) {
        PlatformTimeProvider()
    }

    internal actual fun provideDatabase(): Database {
        return database
    }

    internal actual fun provideSettings(): Settings {
        return settings
    }

    internal actual fun provideTimeProvider(): TimeProvider {
        return timeProvider
    }
}
