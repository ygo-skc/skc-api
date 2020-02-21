package com.rtomyj.yugiohAPI.configuration.exception;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import com.rtomyj.yugiohAPI.configuration.exception.YgoError.Error;
import com.rtomyj.yugiohAPI.helper.LogHelper;
import com.rtomyj.yugiohAPI.helper.constants.ErrConstants;

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
	public final ResponseEntity<YgoError> test(final YgoException exception, final HttpServletRequest request)
	{

		if (exception.getCode() == ErrConstants.NOT_FOUND_DAO_ERR)
		{
			final HttpStatus status = HttpStatus.NOT_FOUND;

			log.info(LogHelper.exceptionLog(request.getRemoteHost(), exception.toString(), request.getRequestURI(), status));
			return new ResponseEntity<>(new YgoError(Error.D001.toString(), Error.D001.name()), status);
		}

		return null;
	}



	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public YgoError onValidationFail(final ConstraintViolationException exception, final HttpServletRequest request)
	{
		log.info(LogHelper.exceptionLog(request.getRemoteHost(), exception.toString(), request.getRequestURI(), HttpStatus.BAD_REQUEST));
		YgoError ygoException = new YgoError(Error.D101.toString(), Error.D101.name());
		return ygoException;
	}

}