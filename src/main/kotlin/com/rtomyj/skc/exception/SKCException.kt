package com.rtomyj.skc.exception

data class SKCException(override val message: String, val errorType: ErrorType) : RuntimeException()