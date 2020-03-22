package com.rtomyj.yugiohAPI.helper.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YgoException extends Exception
{
	private static final long serialVersionUID = 1L;

	private  String code;
	private String message;
}