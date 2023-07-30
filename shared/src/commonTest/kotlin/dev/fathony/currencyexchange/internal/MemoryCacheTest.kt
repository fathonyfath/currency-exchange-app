package dev.fathony.currencyexchange.internal

import app.cash.turbine.test
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import dev.fathony.currencyexchange.Currencies
import dev.fathony.currencyexchange.Currency
import dev.fathony.currencyexchange.Rate
import dev.fathony.currencyexchange.Rates
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
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
    fun testCurrenciesCache_conflated() = runTest {
        val cache = MemoryCache()
        cache.putCurrencies(createDummyCurrencies())

        cache.getCurrencies().test {
            awaitItem()
        }
    }

    @Test
    fun testCurrenciesCache_resend_equals() = runTest {
        val cache = MemoryCache()

        cache.getCurrencies().test {
            cache.putCurrencies(createDummyCurrencies())
            awaitItem()
            cache.putCurrencies(createDummyCurrencies())
            awaitItem()
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
    fun testRatesCache_conflated() = runTest {
        val cache = MemoryCache()
        cache.putRates(createRatesForIdr())

        cache.getRatesForCurrency(Currency("idr", "Indonesian rupiah")).test {
            awaitItem()
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

    @Test
    fun testRateCache_update() = runTest {
        val cache = MemoryCache()

        cache.getRatesForCurrency(Currency("idr", "Indonesian rupiah")).test {
            cache.putRates(createRatesForIdr())
            assertEquals(1, awaitItem().size)
            cache.putRate(createRateFromIdrToSgd())
            assertEquals(1, awaitItem().size)
        }
    }

    @Test
    fun testRateCache_insert() = runTest {
        val cache = MemoryCache()

        cache.getRatesForCurrency(Currency("idr", "Indonesian rupiah")).test {
            cache.putRates(createRatesForIdr())
            assertEquals(1, awaitItem().size)
            cache.putRate(createRateFromIdrToJpy())
            assertEquals(2, awaitItem().size)
        }
    }

    @Test
    fun testRateCache_noEvent() = runTest {
        val cache = MemoryCache()

        cache.getRatesForCurrency(Currency("usd", "United States dollar")).test {
            cache.putRates(createRatesForIdr())
            expectNoEvents()

            cache.putRate(createRateFromIdrToSgd())
            expectNoEvents()

            cache.putRate(createRateFromIdrToJpy())
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
                    exchangeRate = BigDecimal.fromDouble(0.000088),
                    lastUpdate = LocalDate(2023, Month.APRIL, 1)
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
                    exchangeRate = BigDecimal.fromDouble(0.000088),
                    lastUpdate = LocalDate(2023, Month.APRIL, 1)
                ),
                Rate(
                    baseCurrency = idrCurrency,
                    targetCurrency = jpyCurrency,
                    exchangeRate = BigDecimal.fromDouble(0.009425),
                    lastUpdate = LocalDate(2023, Month.APRIL, 1)
                )
            )
        )
    }

    private fun createRateFromIdrToSgd(): Rate {
        val idrCurrency = Currency("idr", "Indonesian rupiah")
        val sgdCurrency = Currency("sgd", "Singapore dollar")
        return Rate(
            baseCurrency = idrCurrency,
            targetCurrency = sgdCurrency,
            exchangeRate = BigDecimal.fromDouble(0.000088),
            lastUpdate = LocalDate(2023, Month.APRIL, 5)
        )
    }

    private fun createRateFromIdrToJpy(): Rate {
        val idrCurrency = Currency("idr", "Indonesian rupiah")
        val jpyCurrency = Currency("jpy", "Japanese yen")
        return Rate(
            baseCurrency = idrCurrency,
            targetCurrency = jpyCurrency,
            exchangeRate = BigDecimal.fromDouble(0.009425),
            lastUpdate = LocalDate(2023, Month.APRIL, 1)
        )
    }
}