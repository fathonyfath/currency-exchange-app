package dev.fathony.currencyexchange.settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.datetime.Instant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class CurrencyExchangeSettings(private val settings: Settings) {

    private val json: Json = Json

    fun setLastFetchGetCurrencies(time: Instant) {
        settings[LastFetchGetCurrencies] = json.encodeToString(time)
    }

    fun getLastFetchGetCurrencies(): Instant? {
        val lastFetch: String? = settings[LastFetchGetCurrencies]
        return lastFetch?.let { json.decodeFromString(it) }
    }

    fun setSavedCurrencies(savedCurrencies: SavedCurrencies) {
        settings[SavedCurrencies] = json.encodeToString(savedCurrencies)
    }

    fun getSavedCurrencies(): SavedCurrencies? {
        val savedCurrencies: String? = settings[SavedCurrencies]
        return savedCurrencies?.let { json.decodeFromString(savedCurrencies) }
    }

    private companion object {
        const val LastFetchGetCurrencies = "LastFetchGetCurrencies"
        const val SavedCurrencies = "SavedCurrencies"
    }
}
