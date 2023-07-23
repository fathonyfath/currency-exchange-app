package dev.fathony.currencyexchange.internal

import kotlinx.datetime.Instant

internal interface TimeProvider {

    fun provideCurrentTime(): Instant
}
