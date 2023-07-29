package dev.fathony.currencyexchange.internal

import dev.fathony.currencyexchange.Currencies
import dev.fathony.currencyexchange.Currency
import dev.fathony.currencyexchange.Rates
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

internal class MemoryCache {

    private val currenciesCache: MutableSharedFlow<Currencies> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val ratesCache: MutableStateFlow<Map<Currency, Rates>> = MutableStateFlow(emptyMap())

    fun getCurrencies(): Flow<Currencies> {
        return currenciesCache
    }

    fun getRatesForCurrency(currency: Currency): Flow<Rates> {
        return ratesCache.map { it[currency] }
            .filterNotNull()
    }

    fun putCurrencies(currencies: Currencies) {
        currenciesCache.tryEmit(currencies)
    }

    fun putRates(rates: Rates) {
        val ratesMap = ratesCache.value.toMutableMap()
        ratesMap[rates.baseCurrency] = rates
        ratesCache.value = ratesMap
    }
}
