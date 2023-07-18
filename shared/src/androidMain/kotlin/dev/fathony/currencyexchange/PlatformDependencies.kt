package dev.fathony.currencyexchange

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import dev.fathony.currencyexchange.db.DriverFactory
import dev.fathony.currencyexchange.internal.SettingsName
import dev.fathony.currencyexchange.sqldelight.Database

actual class PlatformDependencies(context: Context) {

    private val driverFactory: DriverFactory = DriverFactory(context)

    private val database: Database by lazy(LazyThreadSafetyMode.NONE) {
        Database(driverFactory.createDriver())
    }

    private val settingsFactory: Settings.Factory = SharedPreferencesSettings.Factory(context)

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