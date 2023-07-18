package dev.fathony.currencyexchange

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import dev.fathony.currencyexchange.db.DriverFactory
import dev.fathony.currencyexchange.internal.SettingsName
import dev.fathony.currencyexchange.sqldelight.Database
import platform.Foundation.NSUserDefaults

actual class PlatformDependencies {

    private val driverFactory: DriverFactory = DriverFactory()

    private val database: Database by lazy(LazyThreadSafetyMode.NONE) {
        Database(driverFactory.createDriver())
    }

    private val settingsFactory: Settings.Factory = NSUserDefaultsSettings.Factory()

    private val settings: Settings by lazy(LazyThreadSafetyMode.NONE) {
        settingsFactory.create(SettingsName)
    }

    internal actual fun provideDatabase(): Database {
        return database
    }

    internal actual fun provideSettings(): Settings {
        return settings
    }
}
