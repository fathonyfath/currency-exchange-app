# Currency Exchange App

## Motivation

We designed this project to explore Kotlin Multiplatform for Mobile applications. It leverages various concepts typical to mobile app development.

For handling network requests, we use [Ktor](https://ktor.io/docs/http-client-engines.html) because of its multiplatform support.

In terms of data persistence, two methods are employed:
1. Relational database persistence with [SQLDelight](https://github.com/cashapp/sqldelight).
2. Key-value storage through [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings).

We selected the currency exchange scenario specifically due to the potential complexities with large currency numbers. Since Kotlin lacks native `BigInteger` and `BigDecimal`, we integrated [Multiplatform Bignum](https://github.com/ionspin/kotlin-multiplatform-bignum). This offers `BigInteger` and `BigDecimal` implementations compatible with both Android and iOS. Our goal was to assess the utility of such an abstraction in Kotlin.

## Endpoints

We utilize the [currency-api](https://github.com/fawazahmed0/currency-api) for free currency rate data.

## APIs

The primary entry to the library is the `CurrencyExchange` class. This requires dependencies defined in the platform-specific `PlatformDependencies` class.

### `getCurrencies()`

This method provides a list of accessible currencies. It yields a Flow that emits once data becomes available. Initially, cached data (from the database) is emitted, followed by an API refresh if the cached data is over a day old.

Note: This Flow might emit an exception, which happens if there's no cached data and an API update fails.

### `refreshCurrencies()`

This forces a currency refresh. Successful refreshes result in new currencies being emitted by the `getCurrencies()` Flow. As a `suspend` function, it will either return nothing (on success) or an exception (on failure).

### `getRates(Currency)`

This emits a Flow detailing currency exchange rates. For instance, if `Currency("USD")` is input, rates for converting USD to other currencies are emitted. Users can only input currencies that are output from the `getCurrencies()` method.

This behaves similarly to `getCurrencies()`, prioritizing cached data and attempting an API refresh if cached data is older than a day. If no cached data exists and an API update fails, an exception is returned.

### `refreshRates(Currency)`

This method forces a refresh of exchange rates for a specific currency. If successful, new rates are emitted by the `getRates(Currency)` Flow.

Its behavior mirrors `refreshCurrencies()`: it returns nothing upon success, but yields an exception if the refresh operation fails.

### `getRate(from: Currency, target: Currency)`

This yields a Flow showing the conversion rate between two currencies. For example, inputting `Currency("USD")` and `Currency("SGD")` would emit the conversion rate from USD to SGD.

Like the other `get` methods, it tries to emit cached data first, followed by an API refresh if the cached data is more than a day old. If there's no cached data and the API update fails, an exception is thrown.

### `refreshRate(from: Currency, target: Currency)`

This forces a refresh of the conversion rate between two currencies. Success means the new rate is emitted by the `getRate(from, target)` Flow.

Its behavior is consistent with `refreshCurrencies()`: it returns nothing on success, but issues an exception if the refresh fails.
