package com.rtomyj.skc.exception

import com.rtomyj.skc.enums.ErrorType
import lombok.Builder
import lombok.Data
import lombok.EqualsAndHashCode
import org.springframework.http.HttpStatus
import java.lang.RuntimeException

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
class YgoException(override val message: String, val httpStatus: HttpStatus, val errorType: ErrorType)
	: RuntimeException() {
}