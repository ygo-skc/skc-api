package com.rtomyj.yugiohAPI.configuration;

import javax.validation.ConstraintViolationException;

import com.rtomyj.yugiohAPI.helper.constants.ErrConstants;
import com.rtomyj.yugiohAPI.helper.exceptions.Error;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoError;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;


@ControllerAdvice
@Slf4j
public class ExceptionProvider extends ResponseEntityExceptionHandler
{
	@ResponseBody
	@ExceptionHandler(YgoException.class)
	public final ResponseEntity<YgoError> test(final YgoException exception)
	{

		if (exception.getCode() == ErrConstants.NOT_FOUND_DAO_ERR)
		{
			final HttpStatus status = HttpStatus.NOT_FOUND;

			log.error("Exception occurred: {}, responding with: {}", exception.toString(), status);
			return new ResponseEntity<>(new YgoError(Error.D001.toString(), Error.D001.name()), status);
		}

		return null;
	}



	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public YgoError onValidationFail(final ConstraintViolationException exception)
	{
		log.error("Request did not conform to spec. Exception: {}", exception.toString());
		YgoError ygoException = new YgoError(Error.D101.toString(), Error.D101.name());
		return ygoException;
	}

}