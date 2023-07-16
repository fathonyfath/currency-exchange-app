package dev.fathony.currencyexchange.api.exception

class WrappedRequestException(val underlying: Throwable) : RequestException()
