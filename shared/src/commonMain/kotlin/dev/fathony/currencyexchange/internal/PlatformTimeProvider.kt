package dev.fathony.currencyexchange.internal

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

internal class PlatformTimeProvider : TimeProvider {

    override fun provideCurrentTime(): Instant {
        return Clock.System.now()
    }
}
