package dev.fathony.currencyexchange.internal

import dev.fathony.currencyexchange.Currencies
import dev.fathony.currencyexchange.Currency
import dev.fathony.currencyexchange.Rates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

internal class MemoryCache {

    private val currenciesCache: MutableStateFlow<Currencies?> = MutableStateFlow(null)
    private val ratesCache: MutableStateFlow<Map<Currency, Rates>> = MutableStateFlow(emptyMap())

    fun getCurrencies(): Flow<Currencies> {
        return currenciesCache.filterNotNull()
            .distinctUntilChanged()
    }

    fun getRatesForCurrency(currency: Currency): Flow<Rates> {
        return ratesCache.map { it[currency] }
            .filterNotNull()
            .distinctUntilChanged()
    }

    fun putCurrencies(currencies: Currencies) {
        currenciesCache.value = currencies
    }

    fun putRates(currency: Currency, rates: Rates) {
        val ratesMap = ratesCache.value.toMutableMap()
        ratesMap[currency] = rates
        ratesCache.value = ratesMap
    }
}