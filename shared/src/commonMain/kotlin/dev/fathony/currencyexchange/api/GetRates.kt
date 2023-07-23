package dev.fathony.currencyexchange.api

import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.*

internal class GetRates private constructor(
    val countryCode: String,
    val date: LocalDate,
    val rates: List<Rate>
) {

    class Rate(val countryCode: String, val rate: Double)

    companion object {
        fun parse(value: Map<String, JsonElement>): GetRates {
            val date = value.getValue("date")
            val localDate = Json.decodeFromJsonElement<LocalDate>(date)
            val (countryCode, rates) = value.minus("date").toList().first()
            val parsedRates = rates.jsonObject.map { Rate(it.key, it.value.jsonPrimitive.double) }

            return GetRates(countryCode, localDate, parsedRates)
        }
    }
}
