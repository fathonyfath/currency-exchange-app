package dev.fathony.currencyexchange

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.get
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.fathony.currencyexchange.api.CurrencyExchangeApi
import dev.fathony.currencyexchange.db.CurrencyExchangeDatabase
import dev.fathony.currencyexchange.db.DbCurrency
import dev.fathony.currencyexchange.exceptions.NoCachedDataException
import dev.fathony.currencyexchange.internal.MemoryCache
import dev.fathony.currencyexchange.internal.TimeProvider
import dev.fathony.currencyexchange.settings.CurrencyExchangeSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrencyExchange(dependencies: PlatformDependencies) {

    private val api: CurrencyExchangeApi = CurrencyExchangeApi()
    private val database: CurrencyExchangeDatabase =
        CurrencyExchangeDatabase(dependencies.provideDatabase())
    private val settings: CurrencyExchangeSettings =
        CurrencyExchangeSettings(dependencies.provideSettings())
    private val timeProvider: TimeProvider = dependencies.provideTimeProvider()

    private val cache: MemoryCache = MemoryCache()

    fun getCurrencies(): Flow<Result<Currencies, NoCachedDataException>> {
        return channelFlow {
            launch {
                database.getCurrencies()
                    .map { it.get() }
                    .filterNotNull()
                    .filter { it.isNotEmpty() }
                    .map { currencies ->
                        return@map currencies.map { Currency(it.code, it.name) }.toSet()
                    }
                    .map { Currencies(it) }
                    .collect { cache.putCurrencies(it) }
            }
            launch(Dispatchers.Default) {
                val result = database.getCurrencies().first()
                val isEmpty = result is Ok && result.value.isEmpty()
                val isError = result is Err

                if (isEmpty || isError || haveToRefreshCurrencies()) {
                    val currenciesResult = api.getCurrencies()
                        .map { it.values }
                        .map { list -> list.map { Currency(it.key, it.value) }.toSet() }

                    when (currenciesResult) {
                        is Ok -> {
                            database.updateCurrencies(
                                currenciesResult.value.map { DbCurrency(it.code, it.name) }
                            ).onSuccess {
                                settings.setLastFetchGetCountries(timeProvider.provideCurrentTime())
                            }.onFailure {
                                cache.putCurrencies(Currencies(currenciesResult.value))
                            }
                        }

                        is Err -> {
                            if (isEmpty) send(Err(NoCachedDataException()))
                        }
                    }
                }
            }
            launch(Dispatchers.Default) {
                cache.getCurrencies().collect { send(Ok(it)) }
            }
        }
    }

    suspend fun refreshCurrencies(): Result<Unit, Throwable> = withContext(Dispatchers.IO) {
        return@withContext api.getCurrencies()
            .map { it.values }
            .map { list -> list.map { Currency(it.key, it.value) }.toSet() }
            .onSuccess { currencies ->
                database.updateCurrencies(
                    currencies.map { DbCurrency(it.code, it.name) }
                ).onSuccess {
                    settings.setLastFetchGetCountries(timeProvider.provideCurrentTime())
                }.onFailure {
                    cache.putCurrencies(Currencies(currencies))
                }
            }
            .andThen { Ok(Unit) }
    }

    fun getRates(country: Unit) {
        TODO()
    }

    fun getRate(from: Unit, target: Unit) {
        TODO()
    }

    private suspend fun haveToRefreshCurrencies(): Boolean = withContext(Dispatchers.Default) {
        val lastFetchCurrencies = settings.getLastFetchGetCountries() ?: return@withContext true
        val duration = timeProvider.provideCurrentTime().minus(lastFetchCurrencies)
        return@withContext duration.inWholeDays >= 1
    }
}
