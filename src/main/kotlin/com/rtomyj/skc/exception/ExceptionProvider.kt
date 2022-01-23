package com.rtomyj.skc.exception

import com.rtomyj.skc.constant.LogConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
class ExceptionProvider : ResponseEntityExceptionHandler() {
	companion object {
		private val log: Logger = LoggerFactory.getLogger(this::class.java)
	}


	@ResponseBody
	@ExceptionHandler(YgoException::class)
	fun onYgoException(exception: YgoException): ResponseEntity<YgoError> {
		log.error(LogConstants.EXCEPTION_PROVIDER_LOG, exception.message, exception.errorType, exception.errorType.httpStatus)

		return ResponseEntity(
			YgoError(exception.errorType.error, exception.errorType.name), exception.errorType.httpStatus
		)
	}


	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException::class)
	fun onValidationFail(exception: ConstraintViolationException): YgoError {
		log.error("Request did not conform to spec. Constraints violated: {}", exception.toString())

		return YgoError(ErrorType.G001.error, ErrorType.G001.name)
	}
}