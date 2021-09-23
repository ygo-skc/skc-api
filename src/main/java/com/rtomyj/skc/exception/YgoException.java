package com.rtomyj.skc.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
public class YgoException extends RuntimeException
{
	private final String code;
	private final String message;
}