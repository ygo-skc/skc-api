package com.rtomyj.skc.exception

class SKCException(
    message: String,
    val errorType: ErrorType,
) : RuntimeException(message)
