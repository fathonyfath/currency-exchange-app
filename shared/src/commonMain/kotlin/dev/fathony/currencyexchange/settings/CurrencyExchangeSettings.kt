package dev.fathony.currencyexchange.settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class CurrencyExchangeSettings(private val settings: Settings) {

    private val json: Json = Json

    fun setLastFetchGetCountries(time: Instant) {
        settings[LastFetchGetCurrencies] = json.encodeToString(time)
    }

    fun getLastFetchGetCountries(): Instant? {
        val lastFetch: String? = settings[LastFetchGetCurrencies]
        return lastFetch?.let { json.decodeFromString(it) }
    }

    fun setPreferredCountryCodeSource(countryCode: String) {
        settings[PreferredCurrencySource] = countryCode
    }

    fun getPreferredCountryCodeSource(countryCode: String): String? {
        return settings[PreferredCurrencySource]
    }

    fun removePreferredCountryCodeSource() {
        settings.remove(PreferredCurrencySource)
    }

    fun setPreferredCountryCodeTarget(countryCode: String) {
        settings[PreferredCurrencyTarget] = countryCode
    }

    fun getPreferredCountryCodeTarget(): String? {
        return settings[PreferredCurrencyTarget]
    }

    fun removePreferredCountryCodeTarget() {
        settings.remove(PreferredCurrencyTarget)
    }

    private companion object {
        const val LastFetchGetCurrencies = "LastFetchGetCurrencies"
        const val PreferredCurrencySource = "PreferredCurrencySource"
        const val PreferredCurrencyTarget = "PreferredCurrencyTarget"
    }
}