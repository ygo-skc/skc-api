package com.rtomyj.skc.exception

import com.rtomyj.skc.constant.LogConstants
import com.rtomyj.skc.enums.ErrorType
import lombok.extern.slf4j.Slf4j
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
@Slf4j
class ExceptionProvider : ResponseEntityExceptionHandler() {
	companion object {
		private val log: Logger = LoggerFactory.getLogger(this::class.java)
	}


	@ResponseBody
	@ExceptionHandler(YgoException::class)
	fun onYgoException(exception: YgoException): ResponseEntity<YgoError> {
		log.error(LogConstants.EXCEPTION_PROVIDER_LOG, exception, exception.httpStatus)

		return ResponseEntity(
			YgoError(exception.errorType.toString(), exception.errorType.name), exception.httpStatus
		)
	}


	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException::class)
	fun onValidationFail(exception: ConstraintViolationException): YgoError {
		log.error("Request did not conform to spec. Constraints violated: {}", exception.toString())

		return YgoError(ErrorType.G001.toString(), ErrorType.G001.name)
	}
}