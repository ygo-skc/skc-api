package com.rtomyj.skc.exception

class DownStreamException(
    message: String,
    val statusCode: Int,
) : RuntimeException(message)
