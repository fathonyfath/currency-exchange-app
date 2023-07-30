package dev.fathony.currencyexchange.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.fathony.currencyexchange.sqldelight.Database
import dev.fathony.currencyexchange.sqldelight.SelectAllRatesForCountry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class CurrencyExchangeDatabase
constructor(private val database: Database) {

    fun getCurrencies(): Flow<Result<List<DbCurrency>, DatabaseException>> {
        return database.currencyQueries.selectAll().asFlow()
            .mapToList(Dispatchers.IO)
            .map { Ok(it) }
            .catch<Result<List<DbCurrency>, DatabaseException>> { emit(Err(DatabaseException(it))) }
    }

    suspend fun updateCurrencies(currencies: List<DbCurrency>): Result<Unit, DatabaseException> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                database.currencyQueries.transaction {
                    database.currencyQueries.deleteAll()
                    currencies.forEach { database.currencyQueries.insert(it.code, it.name) }
                }
                Ok(Unit)
            } catch (e: Exception) {
                Err(DatabaseException(e))
            }
        }

    fun getRatesForCurrency(currency: DbCurrency):
            Flow<Result<List<SelectAllRatesForCountry>, DatabaseException>> {
        return database.rateQueries.selectAllRatesForCountry(currency.code).asFlow()
            .mapToList(Dispatchers.IO)
            .map { Ok(it) }
            .catch<Result<List<SelectAllRatesForCountry>, DatabaseException>> {
                emit(Err(DatabaseException(it)))
            }
    }

    suspend fun updateRates(rates: List<DbRate>): Result<Unit, DatabaseException> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                database.rateQueries.transaction {
                    rates.forEach { rate ->
                        database.rateQueries.upsert(
                            rate.rate,
                            rate.date,
                            rate.from_code,
                            rate.to_code
                        )
                    }
                }
                Ok(Unit)
            } catch (e: Exception) {
                Err(DatabaseException(e))
            }
        }
}
