package com.rtomyj.skc.exception

import java.lang.RuntimeException

data class SKCException(override val message: String, val errorType: ErrorType)
	: RuntimeException()