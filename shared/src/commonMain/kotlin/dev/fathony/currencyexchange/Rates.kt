package dev.fathony.currencyexchange

class Rates
private constructor(
    val baseCurrency: Currency,
    private val rates: Map<Currency, Rate>,
) : Map<Currency, Rate> by rates {

    internal constructor(baseCurrency: Currency, rates: Set<Rate>) : this(
        baseCurrency = baseCurrency,
        rates = rates.associateBy { it.targetCurrency }
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Rates

        if (baseCurrency != other.baseCurrency) return false
        if (rates != other.rates) return false

        return true
    }

    override fun hashCode(): Int {
        var result = baseCurrency.hashCode()
        result = 31 * result + rates.hashCode()
        return result
    }

    override fun toString(): String {
        return "Rates(baseCurrency=$baseCurrency, rates=$rates)"
    }
}
