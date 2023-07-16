package dev.fathony.currencyexchange.api

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import dev.fathony.currencyexchange.api.exception.RequestException
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.appendPathSegments
import io.ktor.serialization.kotlinx.json.json
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import dev.fathony.currencyexchange.api.exception.HttpException
import dev.fathony.currencyexchange.api.exception.WrappedRequestException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.serialization.json.JsonElement

class CurrencyExchangeApi {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }
    private val baseUrl = Url("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/")

    suspend fun getCurrencies(): Result<GetCurrencies, RequestException> {
        val currenciesEndpoint = URLBuilder(baseUrl)
            .appendPathSegments("/currencies.json")
            .build()

        return runCatching { client.get(currenciesEndpoint) }
            .processBody<Map<String, String>>()
            .map(GetCurrencies::parse)
            .processError()
    }

    suspend fun getRates(countryCode: String): Result<GetRates, RequestException> {
        val ratesEndpoint = URLBuilder(baseUrl)
            .appendPathSegments("/currencies/$countryCode.json")
            .build()

        return runCatching { client.get(ratesEndpoint) }
            .processBody<Map<String, JsonElement>>()
            .map(GetRates::parse)
            .processError()
    }

    suspend fun getRate(
        fromCountryCode: String,
        toCountryCode: String
    ): Result<GetRate, RequestException> {
        val rateEndpoint = URLBuilder(baseUrl)
            .appendPathSegments("/currencies/$fromCountryCode/$toCountryCode.json")
            .build()

        return runCatching { client.get(rateEndpoint) }
            .processBody<Map<String, JsonElement>>()
            .map { GetRate.parse(fromCountryCode, it) }
            .processError()
    }

    private suspend inline fun <reified T> Result<HttpResponse, Throwable>.processBody(): Result<T, Throwable> {
        return this.processHttpResponse()
            .map { it.body() }
    }

    private fun Result<HttpResponse, Throwable>.processHttpResponse(): Result<HttpResponse, Throwable> {
        return this.andThen { response ->
            return@andThen if (response.status.isSuccess()) {
                Ok(response)
            } else {
                Err(HttpException(response.status.value, response.status.description))
            }
        }
    }

    private fun <T> Result<T, Throwable>.processError(): Result<T, RequestException> {
        return this.mapError { throwable ->
            return@mapError when (throwable) {
                is HttpException -> throwable
                else -> WrappedRequestException(throwable)
            }
        }
    }
}