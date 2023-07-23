package dev.fathony.currencyexchange.internal

import app.cash.turbine.test
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import dev.fathony.currencyexchange.Currencies
import dev.fathony.currencyexchange.Currency
import dev.fathony.currencyexchange.Rate
import dev.fathony.currencyexchange.Rates
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MemoryCacheTest {

    @Test
    fun testCurrenciesCache_basic() = runTest {
        val cache = MemoryCache()

        cache.getCurrencies().test {
            cache.putCurrencies(createDummyCurrencies())
            val currencies = awaitItem()
            val countryCodes = currencies.map { it.code }.toSet()
            assertEquals(setOf("idr", "sgd", "jpy", "usd"), countryCodes)
        }
    }

    @Test
    fun testCurrenciesCache_resend_equals() = runTest {
        val cache = MemoryCache()

        cache.getCurrencies().test {
            cache.putCurrencies(createDummyCurrencies())
            awaitItem()
            cache.putCurrencies(createDummyCurrencies())
            expectNoEvents()
        }
    }

    @Test
    fun testCurrenciesCache_resend_distinct() = runTest {
        val cache = MemoryCache()

        cache.getCurrencies().test {
            cache.putCurrencies(createDummyCurrencies())
            val countryCodes = awaitItem().map { it.code }.toSet()
            assertEquals(setOf("idr", "sgd", "jpy", "usd"), countryCodes)
            cache.putCurrencies(createDistinctDummyCurrencies())
            val distinctCountryCodes = awaitItem().map { it.code }.toSet()
            assertEquals(setOf("idr", "sgd", "jpy"), distinctCountryCodes)
        }
    }

    @Test
    fun testRatesCache_basic() = runTest {
        val cache = MemoryCache()

        cache.getRatesForCurrency(Currency("idr", "Indonesian rupiah")).test {
            cache.putRates(createRatesForIdr())
            val availableRates = awaitItem().map { it.value.targetCurrency.code }.toSet()
            assertEquals(setOf("sgd"), availableRates)

            cache.putRates(createDistinctRatesForIdr())
            val values = awaitItem()
            val distinctAvailableRates = values.map { it.value.targetCurrency.code }.toSet()
            assertEquals(setOf("sgd", "jpy"), distinctAvailableRates)
        }
    }

    @Test
    fun testRatesCache_noEvent() = runTest {
        val cache = MemoryCache()

        cache.getRatesForCurrency(Currency("usd", "United States dollar")).test {
            cache.putRates(createRatesForIdr())
            expectNoEvents()

            cache.putRates(createDistinctRatesForIdr())
            expectNoEvents()
        }
    }

    private fun createDummyCurrencies(): Currencies {
        return Currencies(
            setOf(
                Currency("idr", "Indonesian rupiah"),
                Currency("sgd", "Singapore dollar"),
                Currency("jpy", "Japanese yen"),
                Currency("usd", "United States dollar")
            )
        )
    }

    private fun createDistinctDummyCurrencies(): Currencies {
        return Currencies(
            setOf(
                Currency("idr", "Indonesian rupiah"),
                Currency("sgd", "Singapore dollar"),
                Currency("jpy", "Japanese yen"),
            )
        )
    }

    private fun createRatesForIdr(): Rates {
        val idrCurrency = Currency("idr", "Indonesian rupiah")
        val sgdCurrency = Currency("sgd", "Singapore dollar")

        return Rates(
            baseCurrency = idrCurrency,
            setOf(
                Rate(
                    baseCurrency = idrCurrency,
                    targetCurrency = sgdCurrency,
                    exchangeRate = BigDecimal.fromDouble(0.000088)
                )
            )
        )
    }

    private fun createDistinctRatesForIdr(): Rates {
        val idrCurrency = Currency("idr", "Indonesian rupiah")
        val sgdCurrency = Currency("sgd", "Singapore dollar")
        val jpyCurrency = Currency("jpy", "Japanese yen")

        return Rates(
            baseCurrency = idrCurrency,
            setOf(
                Rate(
                    baseCurrency = idrCurrency,
                    targetCurrency = sgdCurrency,
                    exchangeRate = BigDecimal.fromDouble(0.000088)
                ),
                Rate(
                    baseCurrency = idrCurrency,
                    targetCurrency = jpyCurrency,
                    exchangeRate = BigDecimal.fromDouble(0.009425)
                )
            )
        )
    }
}