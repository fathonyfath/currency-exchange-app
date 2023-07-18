package dev.fathony.currencyexchange.settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

internal class CurrencyExchangeSettings(private val settings: Settings) {

    fun setPreferredCountryCodeSource(countryCode: String) {
        settings[PreferredCountryCodeSource] = countryCode
    }

    fun getPreferredCountryCodeSource(countryCode: String): String? {
        return settings[PreferredCountryCodeSource]
    }

    fun removePreferredCountryCodeSource() {
        settings.remove(PreferredCountryCodeSource)
    }

    fun setPreferredCountryCodeTarget(countryCode: String) {
        settings[PreferredCountryCodeTarget] = countryCode
    }

    fun getPreferredCountryCodeTarget(): String? {
        return settings[PreferredCountryCodeTarget]
    }

    fun removePreferredCountryCodeTarget() {
        settings.remove(PreferredCountryCodeTarget)
    }

    private companion object {
        const val PreferredCountryCodeSource = "PreferredCountryCodeSource"
        const val PreferredCountryCodeTarget = "PreferredCountryCodeTarget"
    }
}