package com.rtomyj.skc.exception

import com.rtomyj.skc.util.constant.LogConstants
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionProvider {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)
  }


  @ResponseBody
  @ExceptionHandler(SKCException::class)
  fun onYgoException(exception: SKCException): ResponseEntity<SKCError> {
    log.error(
      LogConstants.EXCEPTION_PROVIDER_LOG,
      exception.message,
      exception.errorType,
      exception.errorType.httpStatus
    )

    return ResponseEntity(
      SKCError(exception.errorType.error, exception.errorType.name), exception.errorType.httpStatus
    )
  }


  @ResponseBody
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(ConstraintViolationException::class)
  fun onValidationFail(exception: ConstraintViolationException): SKCError {
    log.error("Request did not conform to spec. Constraints violated: {}", exception.toString())

    return SKCError(ErrorType.G001.error, ErrorType.G001.name)
  }
}