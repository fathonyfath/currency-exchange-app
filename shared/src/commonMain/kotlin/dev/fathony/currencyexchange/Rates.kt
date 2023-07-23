package dev.fathony.currencyexchange

class Rates
private constructor(
    val baseCurrency: Currency,
    private val rates: Map<Currency, Rate>,
) : Map<Currency, Rate> by rates {

    internal constructor(baseCurrency: Currency, rates: Set<Rate>) : this(
        baseCurrency = baseCurrency,
        rates = rates.associateBy { it.baseCurrency }
    )
}
