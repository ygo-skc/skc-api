package com.rtomyj.skc.exception

data class DownStreamException(override val message: String, val statusCode: Int) : RuntimeException()