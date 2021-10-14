package com.rtomyj.skc.exception

import lombok.Builder
import lombok.Data
import lombok.EqualsAndHashCode
import java.lang.RuntimeException

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
class YgoException(override val message: String, val errorType: ErrorType)
	: RuntimeException()