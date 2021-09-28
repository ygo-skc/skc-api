package com.rtomyj.skc.config;

import com.rtomyj.skc.constant.LogConstants;
import com.rtomyj.skc.enums.ErrorType;
import com.rtomyj.skc.exception.YgoError;
import com.rtomyj.skc.exception.YgoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;


@ControllerAdvice
@Slf4j
public class ExceptionProvider extends ResponseEntityExceptionHandler
{
	@ResponseBody
	@ExceptionHandler(YgoException.class)
	public final ResponseEntity<YgoError> test(final YgoException exception)
	{
		log.error(LogConstants.EXCEPTION_PROVIDER_LOG, exception, exception.getHttpStatus());
		return new ResponseEntity<>(new YgoError(exception.getErrorType().toString(), exception.getErrorType().name())
				, exception.getHttpStatus());
	}


	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public YgoError onValidationFail(final ConstraintViolationException exception)
	{
		log.error("Request did not conform to spec. Constraints violated: {}", exception.toString());
		return new YgoError(ErrorType.G001.toString(), ErrorType.G001.name());
	}
}