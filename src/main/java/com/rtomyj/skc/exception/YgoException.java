package com.rtomyj.skc.exception;

import com.rtomyj.skc.enums.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;


@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
public class YgoException extends RuntimeException
{
	private final String message;
	private final HttpStatus httpStatus;
	private final ErrorType errorType;
}