package dev.fathony.currencyexchange

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import co.touchlab.kermit.Logger
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.onSuccess
import com.github.michaelbull.result.runCatching
import dev.fathony.currencyexchange.api.CurrencyExchangeApi
import dev.fathony.currencyexchange.api.GetCurrencies
import dev.fathony.currencyexchange.exceptions.NetworkException
import dev.fathony.currencyexchange.exceptions.NoCachedDataException
import dev.fathony.currencyexchange.internal.MemoryCache
import dev.fathony.currencyexchange.internal.TimeProvider
import dev.fathony.currencyexchange.settings.CurrencyExchangeSettings
import dev.fathony.currencyexchange.sqldelight.Currency
import dev.fathony.currencyexchange.sqldelight.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class CurrencyExchange(dependencies: PlatformDependencies) {

    private val api: CurrencyExchangeApi = CurrencyExchangeApi()
    private val database: Database = dependencies.provideDatabase()
    private val settings: CurrencyExchangeSettings =
        CurrencyExchangeSettings(dependencies.provideSettings())
    private val timeProvider: TimeProvider = dependencies.provideTimeProvider()

    private val cache: MemoryCache = MemoryCache()

    fun getCurrencies(): Flow<Result<List<Currency>, NoCachedDataException>> {
        return database.currencyQueries.selectAll().asFlow()
            .mapToList(Dispatchers.IO)
            .onEach { values ->
                Logger.d { "values is: $values" }
            }
            .map { values ->
                return@map if (values.isNotEmpty()) {
                    Ok(emptyList())
                } else {
                    Err(NoCachedDataException())
                }
            }
    }

    suspend fun refreshCurrencies(): Result<Unit, Throwable> = withContext(Dispatchers.IO) {
        return@withContext api.getCurrencies()
            .mapError { NetworkException() }
            .andThen { putGetCurrenciesToDatabase(it) }
            .onSuccess {
                val time = timeProvider.provideCurrentTime()
                settings.setLastFetchGetCountries(time)
            }
            .andThen { Ok(Unit) }
    }

    fun getRates(country: Unit) {
        TODO()
    }

    fun getRate(from: Unit, target: Unit) {
        TODO()
    }

    private fun putGetCurrenciesToDatabase(data: GetCurrencies): Result<Unit, Throwable> {
        return runCatching {
            database.currencyQueries.transaction {
                data.values.forEach { (currencyCode, currencyName) ->
                    database.currencyQueries.insert(currencyCode, currencyName)
                }
            }
        }
    }
}
