package dev.fathony.currencyexchange

import dev.fathony.currencyexchange.sqldelight.Database

expect class PlatformDependencies {

    internal fun createDatabase(): Database
}
