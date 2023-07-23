package dev.fathony.currencyexchange

class Currencies
internal constructor(
    private val currencies: Set<Currency>
) : Set<Currency> by currencies {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Currencies

        if (currencies != other.currencies) return false

        return true
    }

    override fun hashCode(): Int {
        return currencies.hashCode()
    }

    override fun toString(): String {
        return currencies.toString()
    }
}
