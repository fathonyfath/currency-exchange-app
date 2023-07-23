package dev.fathony.currencyexchange

import com.ionspin.kotlin.bignum.decimal.BigDecimal

class Rate
internal constructor(
    val baseCurrency: Currency,
    val targetCurrency: Currency,
    val exchangeRate: BigDecimal,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Rate

        if (baseCurrency != other.baseCurrency) return false
        if (targetCurrency != other.targetCurrency) return false
        if (exchangeRate != other.exchangeRate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = baseCurrency.hashCode()
        result = 31 * result + targetCurrency.hashCode()
        result = 31 * result + exchangeRate.hashCode()
        return result
    }

    override fun toString(): String {
        return "Rate(baseCurrency=$baseCurrency, targetCurrency=$targetCurrency, exchangeRate=$exchangeRate)"
    }
}