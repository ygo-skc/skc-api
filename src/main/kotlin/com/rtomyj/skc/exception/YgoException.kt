package com.rtomyj.skc.exception

import java.lang.RuntimeException

data class YgoException(override val message: String, val errorType: ErrorType)
	: RuntimeException()