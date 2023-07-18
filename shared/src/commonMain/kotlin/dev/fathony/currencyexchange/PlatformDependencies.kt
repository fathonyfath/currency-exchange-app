package dev.fathony.currencyexchange

import com.russhwolf.settings.Settings
import dev.fathony.currencyexchange.sqldelight.Database

expect class PlatformDependencies {

    internal fun provideDatabase(): Database
    internal fun provideSettings(): Settings
}
