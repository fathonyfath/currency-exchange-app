package dev.fathony.currencyexchange.api.exception

internal class WrappedRequestException(val underlying: Throwable) : RequestException()
