package dev.fathony.currencyexchange.api.exception

internal class HttpException(val code: Int, override val message: String) : RequestException()
