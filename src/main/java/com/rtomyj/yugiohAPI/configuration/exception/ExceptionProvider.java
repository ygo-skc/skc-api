package com.rtomyj.yugiohAPI.configuration.exception;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import com.rtomyj.yugiohAPI.configuration.YgoConstants;
import com.rtomyj.yugiohAPI.helper.LogHelper;

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
		log.info(LogHelper.exceptionLog(request.getRemoteHost(), exception.toString(), request.getRequestURI(), HttpStatus.NOT_FOUND));

		if (exception.getCode() == YgoConstants.DAO_NOT_FOUND_ERR)
			return new ResponseEntity<>(new YgoError("Requested resource was not found in database.", HttpStatus.NOT_FOUND.toString()), HttpStatus.NOT_FOUND);

		return null;
	}



	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public YgoError onValidationFail(ConstraintViolationException exception)
	{
		System.out.println(exception);
		YgoError ygoException = new YgoError("Request was not formed correctly or did not follow expected pattern.", "400");
		return ygoException;
	}

}