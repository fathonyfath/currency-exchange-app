package dev.fathony.currencyexchange.api.exception

class HttpException(val code: Int, override val message: String) : RequestException()
