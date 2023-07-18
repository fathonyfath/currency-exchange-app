package dev.fathony.currencyexchange.internal

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

internal class PlatformTimeProvider : TimeProvider {

    override fun provideTime(): Instant {
        return Clock.System.now()
    }
}
