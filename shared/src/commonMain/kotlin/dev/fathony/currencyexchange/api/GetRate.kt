package dev.fathony.currencyexchange.api

import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonPrimitive

internal class GetRate private constructor(
    val fromCountryCode: String,
    val toCountryCode: String,
    val date: LocalDate,
    val rate: Double
) {

    companion object {
        fun parse(fromCountryCode: String, values: Map<String, JsonElement>): GetRate {
            val date = values.getValue("date")
            val localDate = Json.decodeFromJsonElement<LocalDate>(date)
            val (toCountryCode, rate) = values.minus("date").toList().first()
            val parsedRate = rate.jsonPrimitive.double

            return GetRate(fromCountryCode, toCountryCode, localDate, parsedRate)
        }
    }
}
