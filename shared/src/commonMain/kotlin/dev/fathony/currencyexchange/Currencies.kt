package dev.fathony.currencyexchange

class Currencies
internal constructor(
    private val currencies: Set<Currency>
) : Set<Currency> by currencies
