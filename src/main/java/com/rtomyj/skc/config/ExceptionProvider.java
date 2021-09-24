package com.rtomyj.skc.config;

import com.rtomyj.skc.constant.ErrConstants;
import com.rtomyj.skc.constant.LogConstants;
import com.rtomyj.skc.enums.ErrorTypes;
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
		if (exception.getCode().equals(ErrConstants.NOT_FOUND_DAO_ERR))
		{

			final HttpStatus status = HttpStatus.NOT_FOUND;
			log.error(LogConstants.EXCEPTION_PROVIDER_LOG, exception, status);
			return new ResponseEntity<>(new YgoError(ErrorTypes.D001.toString(), ErrorTypes.D001.name()), status);

		}
		return null;
	}


	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public YgoError onValidationFail(final ConstraintViolationException exception)
	{
		log.error("Request did not conform to spec. Constraints violated: {}", exception.toString());
		return new YgoError(ErrorTypes.D101.toString(), ErrorTypes.D101.name());
	}
}