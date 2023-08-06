package dev.fathony.currencyexchange

import com.russhwolf.settings.Settings
import dev.fathony.currencyexchange.internal.TimeProvider
import dev.fathony.currencyexchange.internal.db.sqldelight.Database

expect class PlatformDependencies {

    internal fun provideDatabase(): Database
    internal fun provideSettings(): Settings
    internal fun provideTimeProvider(): TimeProvider
}
