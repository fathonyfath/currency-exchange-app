package dev.fathony.currencyexchange.settings

import kotlinx.serialization.Serializable

@Serializable
internal data class SavedCurrencies(
    val sourceCurrencyCode: String?,
    val targetCurrencyCode: String?
)
