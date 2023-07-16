package dev.fathony.currencyexchange.api

class GetCurrencies private constructor(values: Map<String, String>) {

    val values: Map<String, String> = values.toMap()
    val countryCodes = this.values.keys

    operator fun get(key: String): String? = values[key]

    companion object {
        fun parse(values: Map<String, String>): GetCurrencies {
            return GetCurrencies(values)
        }
    }
}
