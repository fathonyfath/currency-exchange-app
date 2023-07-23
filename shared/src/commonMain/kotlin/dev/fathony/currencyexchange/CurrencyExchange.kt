package dev.fathony.currencyexchange

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import co.touchlab.kermit.Logger
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.expect
import com.github.michaelbull.result.fold
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.onSuccess
import com.russhwolf.settings.Settings
import dev.fathony.currencyexchange.api.CurrencyExchangeApi
import dev.fathony.currencyexchange.exceptions.NoCachedDataException
import dev.fathony.currencyexchange.internal.TimeProvider
import dev.fathony.currencyexchange.sqldelight.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class CurrencyExchange(dependencies: PlatformDependencies) {

    private val api: CurrencyExchangeApi = CurrencyExchangeApi()
    private val database: Database = dependencies.provideDatabase()
    private val settings: Settings = dependencies.provideSettings()
    private val timeProvider: TimeProvider = dependencies.provideTimeProvider()

    fun getCurrencies(): Flow<Result<Unit, NoCachedDataException>> {
        return database.currencyQueries.selectAll().asFlow()
            .mapToList(Dispatchers.IO)
            .onEach { values ->
                Logger.d { "values is: $values" }
            }
            .map { values ->
                return@map if (values.isNotEmpty()) {
                    Ok(Unit)
                } else {
                    Err(NoCachedDataException())
                }
            }
    }

    suspend fun refreshCurrencies(): Result<Unit, Throwable> = TODO()

    fun getRates(country: Unit) {
        TODO()
    }

    fun getRate(from: Unit, target: Unit) {
        TODO()
    }
}
